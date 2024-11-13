'''<document> ::= <element>
<element> ::= <start_tag> <content> <end_tag>
<start_tag> ::= <tag_name> | <tag_name> <attributes>
<end_tag> ::= </<tag_name>>
<content> ::= <text> | <element> | <content> <element>
<attributes> ::= <attribute> | <attribute> <attributes>
<attribute> ::= <attr_name>="value"
<tag_name> ::= "item" | "name" | "age"  // и так далее для всех возможных имен тегов
<text> ::= "..."  // строка текста, оставляем для специфики
'''
import json

def json_to_xml(json_obj, line_padding=""):
    """Конвертирует JSON в XML."""
    xml_str = ""

    if isinstance(json_obj, dict):
        for tag_name in json_obj:
            xml_str += f"{line_padding}<{tag_name}>\n"
            xml_str += json_to_xml(json_obj[tag_name], line_padding + "  ")
            xml_str += f"{line_padding}</{tag_name}>\n"
    elif isinstance(json_obj, list):
        for sub_elem in json_obj:
            xml_str += json_to_xml(sub_elem, line_padding)
    else:
        xml_str += f"{line_padding}{json_obj}\n"

    return xml_str

# Чтение JSON из файла
with open('q.json', 'r') as json_file:
    json_data = json.load(json_file)

# Конвертация JSON в XML
xml_result = json_to_xml(json_data)

# Запись XML в файл
with open('outdop3.xml', 'w') as xml_file:
    xml_file.write(xml_result)

print("Конвертация завершена! XML сохранен в output.xml")
