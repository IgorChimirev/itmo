import json
import time
start_time = time.time()
import json
import xml.etree.ElementTree as ET

# Грамматика JSON для разбора
# json -> obj | array
# obj -> { (pair ( , pair )*)? }
# pair -> string : value
# array -> [ (value ( , value )*)? ]
# value -> string | number | obj | array | true | false | null

class JsonToXmlConverter:
    def __init__(self, json_data):
        self.json_data = json_data
        
    def convert(self, root_tag='root'):
        root_element = ET.Element(root_tag)
        self._convert_to_xml(self.json_data, root_element)
        return root_element

    def _convert_to_xml(self, data, parent):
        if isinstance(data, dict):
            for key, value in data.items():
                child = ET.SubElement(parent, key)
                self._convert_to_xml(value, child)
        elif isinstance(data, list):
            for item in data:
                child = ET.SubElement(parent, 'item')  # Поддержка массива
                self._convert_to_xml(item, child)
        else:
            parent.text = str(data)

# Чтение JSON данных из файла
json_file = "q.json"  # Укажите здесь имя вашего JSON файла
with open(json_file, 'r') as f:
    data = json.load(f)

# Преобразуем JSON в XML
converter = JsonToXmlConverter(data)
xml_tree = converter.convert()

# Выводим результат в файл XML
xml_output_file = "out.xml"  # Укажите здесь имя выходного XML файла
tree = ET.ElementTree(xml_tree)
tree.write(xml_output_file, encoding='utf-8', xml_declaration=True)

print(f"XML успешно записан в файл: {xml_output_file}")

end_time = time.time()
elapsed_time = end_time - start_time  # Исчисляем прошедшее время
print(f"Время выполнения: {elapsed_time} секунд")
