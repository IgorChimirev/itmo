import re
import time
start_time = time.time()
def sanitize_key(key):
    """Очистка ключа, замена недопустимых символов на подчеркивание."""
    # Заменяем недопустимые символы на подчеркивание
    return re.sub(r'[^a-zA-Z0-9_]', '_', key)

def json_to_xml(data, root_element):
    xml_content = f"<{sanitize_key(root_element)}>\n"
    
    for lesson in data["lessons"]:
        xml_content += "  <lesson>\n"
        for key, value in lesson.items():
            xml_content += f"    <{sanitize_key(key)}>{value}</{sanitize_key(key)}>\n"
        xml_content += "  </lesson>\n"
    
    xml_content += f"</{sanitize_key(root_element)}>\n"  # Закрывающий тег корневого элемента
    return xml_content

def read_json_file(filename):
    with open(filename, 'r', encoding='utf-8') as f:
        content = f.read().strip()
        # Использование регулярных выражений для замены
        content = re.sub(r'\btrue\b', 'True', content)
        content = re.sub(r'\bfalse\b', 'False', content)
        content = re.sub(r'\bnull\b', 'None', content)
    return eval(content)  # Используем eval для загрузки JSON-подобных данных

def write_xml_file(filename, xml_content):
    with open(filename, 'w', encoding='utf-8') as f:
        f.write(xml_content)

def json_to_xml_file(json_file, xml_file, root_element='root'):
    json_data = read_json_file(json_file)
    xml_content = json_to_xml(json_data, root_element)
    write_xml_file(xml_file, xml_content)

# Пример использования
json_to_xml_file('q.json', 'out.xml')
end_time = time.time()
elapsed_time = end_time - start_time  # Исчисляем прошедшее время
print(f"Время выполнения: {elapsed_time} секунд")
