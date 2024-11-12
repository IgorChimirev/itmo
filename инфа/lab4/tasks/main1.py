import time

start_time = time.time()
def sanitize_key(key):
    valid_key = ""
    for char in key:
        if char.isalnum() or char == '_':
            valid_key += char
        else:
            valid_key += '_'
    
    # Убираем повторяющиеся подчеркивания
    while '__' in valid_key:
        valid_key = valid_key.replace('__', '_')
    
    # Удаляем подчеркивание в начале и конце строки
    valid_key = valid_key.strip('_')

    return valid_key


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
        # Замена true, false, null на соответствующие значения Python
        content = content.replace('true', 'True').replace('false', 'False').replace('null', 'None')
    return eval(content)  # Используем eval для загрузки JSON-подобных данных

def write_xml_file(filename, xml_content):
    with open(filename, 'w', encoding='utf-8') as f:
        f.write("<?xml version='1.0' encoding='utf-8'?>\n")  # Добавляем строку с заголовком XML
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

