import re

def task1(string):
    '''
    468013 % 6 = 1 => Глаза: ;
    468013 % 4 = 1 => Нос: <
    468013 % 8 = 5 => Рот: /
    '''
    patt= r';<\/'

    return len(re.findall(patt, string))

inp1 = ';</;</;</;</;</' #5
inp2 = ';</;</;</'#3
inp3 = 'Я люблю информатику ;</ !!!!!!!;</;</' #3
inp4 = '[];</ hi ;;;;</      ;</;</;</;</' #6
inp5 = ';</' # 1
print(task1(inp1),task1(inp2),task1(inp3),task1(inp4),task1(inp5))
