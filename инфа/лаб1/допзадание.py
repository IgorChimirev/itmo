import math
def f3(a,b):#если надо перевести из симметричной сс в 10 сс
    a = a.replace('{','').replace('}','')
    a = list(a)
    for i in range(len(a)-1):
        if a[i] == '^':
            a[i + 1] = '-' + a[i + 1]
    for i in a:
        if i == '^':
            a.remove(i)
            
    a = [int(i) for i in a]
    print(a)
    res = 0
    for i in range(len(a)):
        res = res + a[i] * (b**(len(a)-i -1))
    return res
        
    
def f1(a):
    n = ''
    while a != 0:
        n = str(a%10) + n
        a = math.ceil(a/(-10))
    return n
        
    
w = [0,1]
for i in range(100):
    w.append(w[-1] + w[-2])
w = w[2:]
def f2(a):
    res = []
    for f in reversed(w):
        if f <= a:
            a-=f
            res.append('1')
        elif res:
            res.append('0')
    return ''.join(res)
def f4(a,b):
    q = int(b[0])
    s = ''
    while a > 0:
        s = str(a%q) + s
        a//=q
    return s
def f5(a):
    q = reversed(list(a))
    r = 0
    q = list(int(i) for i in q)
    for i in range(len(q)):
        if q[i] == 1:
            r += w[i]
    return r
e,r,t = map(str,input().split())
if t == 'фиб' or t == '-10' or t == '7с' or t == '9с':
    e = int(e)
if t == 'фиб':
    print(f2(e))
elif t == '-10':
    print(f1(e))
elif t == '7с' or t == '9с':
    print(f4(e,t))
elif r == 'фиб':
    print(f5(e))
