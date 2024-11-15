import time

start_time = time.time()
def sankey(key):
    valid_key = ""
    for char in key:
        if char.isalnum() or char == '_':
            valid_key += char
        else:
            valid_key += '_'
    
   
    while '__' in valid_key:
        valid_key = valid_key.replace('__', '_')
    
   
    valid_key = valid_key.strip('_')

    return valid_key


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
        
        content = content.replace('true', 'True').replace('false', 'False').replace('null', 'None')
    return eval(content)  

def write_xml_file(filename, xml_content):
    with open(filename, 'w', encoding='utf-8') as f:
        f.write("<?xml version='1.0' encoding='utf-8'?>\n") 
        f.write(xml_content)

def json_to_xml_file(json_file, xml_file, root_element='root'):
    json_data = read_json_file(json_file)
    xml_content = json_to_xml(json_data, root_element)
    write_xml_file(xml_file, xml_content)


json_to_xml_file('q.json', 'out.xml')

end_time = time.time()
eltime = end_time - start_time  
print(f"Время выполнения: {eltime} секунд")

