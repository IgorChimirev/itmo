import json
import time
start_time = time.time()
import json
import xml.etree.ElementTree as ET

# Грамматика JSON 
# json -> obj | array
# obj -> { (pair ( , pair )*)? }
# pair -> string : value
# array -> [ (value ( , value )*)? ]
# value -> string | number | obj | array | true | false | null

class JsonToXml:
    def __init__(self, json_data):
        self.json_data = json_data
        
    def convert(self, root_tag='root'):
        root_element = ET.Element(root_tag)
        self.convert_to_xml(self.json_data, root_element)
        return root_element

    def convert_to_xml(self, data, parent):
        if isinstance(data, dict):
            for key, value in data.items():
                child = ET.SubElement(parent, key)
                self.convert_to_xml(value, child)
        elif isinstance(data, list):
            for item in data:
                child = ET.SubElement(parent, 'item')  
                self.convert_to_xml(item, child)
        else:
            parent.text = str(data)


json_file = "q.json"  
with open(json_file, 'r') as f:
    data = json.load(f)


converter = JsonToXml(data)
xml_tree = converter.convert()


xml_output_file = "outdop3.xml"  
tree = ET.ElementTree(xml_tree)
tree.write(xml_output_file, encoding='utf-8', xml_declaration=True)



end_time = time.time()
eltime = end_time - start_time  
print(f"Время выполнения: {eltime} секунд")
