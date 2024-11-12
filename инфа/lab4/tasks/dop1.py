from dicttoxml import dicttoxml
import json
import xml.etree.ElementTree as ET
import time

start_time = time.time()

def json_to_xml(json_file, xml_file):
    # Чтение JSON данных из файла
    with open(json_file, 'r') as f:
        data = json.load(f)

    # Преобразование данных в XML
    xml_data = dicttoxml(data, custom_root='root', attr_type=False)

    # Запись XML данных в файл
    with open(xml_file, 'wb') as f:
        f.write(xml_data)

if __name__ == "__main__":
    json_file = 'q.json'  # Замените на имя вашего JSON файла
    xml_file = 'out.xml'     # Замените на желаемое имя XML файла
    json_to_xml(json_file, xml_file)


end_time = time.time()
elapsed_time = end_time - start_time  # Исчисляем прошедшее время
print(f"Время выполнения: {elapsed_time} секунд")
