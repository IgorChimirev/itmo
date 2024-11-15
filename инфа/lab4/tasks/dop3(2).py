'''<document> ::= <element>
<element> ::= <start_tag> <content> <end_tag>
<start_tag> ::= <tag_name> | <tag_name> <attributes>
<end_tag> ::= </<tag_name>>
<content> ::= <text> | <element> | <content> <element>
<attributes> ::= <attribute> | <attribute> <attributes>
<attribute> ::= <attr_name>="value"
<tag_name> ::= <string>  // любое имя тега
<text> ::= <string>  // текстовое содержимое

'''
import json

def json_to_xml(json_obj, line_padding=""):
    if isinstance(json_obj, dict):
        xml_str = ""
        for tag_name, value in json_obj.items():
            
            xml_str += f"{line_padding}<{tag_name}>\n"
            
            xml_str += json_to_xml(value, line_padding + "  ")
            
            xml_str += f"{line_padding}</{tag_name}>\n"
        return xml_str
    elif isinstance(json_obj, list):
        xml_str = ""
        for item in json_obj:
            xml_str += json_to_xml(item, line_padding)
        return xml_str
    else:
        
        return f"{line_padding}{json_obj}\n"


with open('input.json', 'r') as json_file:
    json_data = json.load(json_file)


xml_result = json_to_xml(json_data)


with open('output.xml', 'w') as xml_file:
    xml_file.write(xml_result)



