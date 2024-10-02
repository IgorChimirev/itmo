inp = input('Введите набор из 7 цифр «0» и «1», записанных подряд: ')

if set(inp) != {'0', '1'} or len(inp) != 7:
    print('Введённая строка должна быть набором из 7 цифр "0" и "1".')
    exit(1)

bits = list(map(int, list(inp)))

s1 = (bits[0] + bits[2] + bits[4] + bits[6]) % 2
s2 = (bits[1] + bits[2] + bits[5] + bits[6]) % 2
s3 = (bits[3] + bits[4] + bits[5] + bits[6]) % 2
syndrome = (s1, s2, s3)

if syndrome != (0, 0, 0):
    index = int(''.join(map(str, syndrome[::-1])), 2)
    error_symbol = {1: 'r1', 2: 'r2', 3: 'i1', 4: 'r3', 5: 'i2', 6: 'i3', 7: 'i4'}[index]
    print(f'В сообщении ошибка!\nОшибка в символе {error_symbol}')
    if error_symbol[0] =='r':
        print([bits[2], bits[4], bits[5], bits[6]])
        exit()
    ind = int(error_symbol[1]) - 1
    result = [bits[2], bits[4], bits[5], bits[6]]
    result[ind] = (result[ind] + 1) % 2
else:
    print('В сообщении нет ошибок!')
    result = [bits[2], bits[4], bits[5], bits[6]]
if error_symbol[0] !='r':
    print(f'Правильное сообщение: {"".join(map(str, result))}')
