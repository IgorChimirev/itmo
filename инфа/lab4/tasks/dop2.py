import re
import time
start_time = time.time()
def sankey(key):
    
    return re.sub(r'[^a-zA-Z0-9_]', '_', key)

def json_to_xml(data, root_element):
    xml_content = f"<{sankey(root_element)}>\n"
    
    for lesson in data["lessons"]:
        xml_content += "  <lesson>\n"
        for key, value in lesson.items():
            xml_content += f"    <{sankey(key)}>{value}</{sankey(key)}>\n"
        xml_content += "  </lesson>\n"
    
    xml_content += f"</{sankey(root_element)}>\n"  
    return xml_content

def read_json_file(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read().strip()
        
        content = re.sub(r'\btrue\b', 'True', content)
        content = re.sub(r'\bfalse\b', 'False', content)
        content = re.sub(r'\bnull\b', 'None', content)
    return eval(content) 

def write_xml_file(filename, xml_content):
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(xml_content)

def json_to_xml_file(json_file, xml_file, root_element='root'):
    json_data = read_json_file(json_file)
    xml_content = json_to_xml(json_data, root_element)
    write_xml_file(xml_file, xml_content)


json_to_xml_file('q.json', 'outdop2.xml')
end_time = time.time()
eltime = end_time - start_time
print(f"Время выполнения: {eltime} секунд")
