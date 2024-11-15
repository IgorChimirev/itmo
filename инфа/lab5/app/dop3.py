import pandas as pd
import plotly.graph_objs as go


data = pd.read_csv('data.csv', delimiter=",", quotechar='"')


data.columns = data.columns.str.strip()


data['<DATE>'] = pd.to_datetime(data['<DATE>'], format='%d/%m/%y', errors='coerce')


dates = { '14/09/18': [], '16/10/18': [], '14/11/18': [], '14/12/18': [] }

for _, row in data.iterrows():
    date_str = row['<DATE>'].strftime('%d/%m/%y')
    if date_str in dates:
        dates[date_str].append({
            'Open': row['<OPEN>'],
            'High': row['<HIGH>'],
            'Low': row['<LOW>'],
            'Close': row['<CLOSE>']
        })


fig = go.Figure()

for date_str, values in dates.items():
    
    open_values = [v['Open'] for v in values]
    high_values = [v['High'] for v in values]
    low_values = [v['Low'] for v in values]
    close_values = [v['Close'] for v in values]

    
    fig.add_trace(go.Box(y=open_values, name=f'{date_str} - Open'))
    fig.add_trace(go.Box(y=high_values, name=f'{date_str} - High'))
    fig.add_trace(go.Box(y=low_values, name=f'{date_str} - Low'))
    fig.add_trace(go.Box(y=close_values, name=f'{date_str} - Close'))


fig.update_layout(legend=dict(yanchor="top", orientation="h", y=1.2))
fig.update_xaxes(tickangle=90, title_standoff=25)


fig.show()
