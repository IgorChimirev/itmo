from dicttoxml import dicttoxml
import json
import xml.etree.ElementTree as ET
import time

start_time = time.time()

def json_to_xml(json_file, xml_file):

    with open(json_file, 'r') as f:
        data = json.load(f)


    xml_data = dicttoxml(data, custom_root='root', attr_type=False)


    with open(xml_file, 'wb') as f:
        f.write(xml_data)

if __name__ == "__main__":
    json_file = 'q.json' 
    xml_file = 'outdop1.xml'     
    json_to_xml(json_file, xml_file)


end_time = time.time()
el_time = end_time - start_time  
print(f"Время выполнения: {el_time} секунд")

