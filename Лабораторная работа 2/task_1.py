money_capital = 20000  # Подушка безопасности
salary = 5000  # Ежемесячная зарплата
spend = 6000  # Траты за первый месяц
increase = 0.05  # Ежемесячный рост цен

budget = money_capital + salary
months = 1
budget -= spend  # бюджет после первого месяца

while True:
    budget += salary
    spend += spend * increase
    if budget < spend:
        break
    budget -= spend
    months += 1

print("Количество месяцев, которое можно протянуть без долгов:", months)
