## training_ylab<br>
### для сборки проекта нужно устиновить зависимости из pom.xml файла<br>
### запустить файл main.java (src\main\java)<br>
### после запуска программы в косоль будут выведены доступные команды<br>
### пример<br>
####MENU<br>
1: регистрация игрока<br>
2: авторизация игрока<br> 
3: текущий баланс игрока<br>
4: дебет/снятие средств игрока<br>
5: кредит на игрока<br>
6: просмотр истории пополнения/снятия средств игроком<br>
7: аудит действий игрока<br>
8: выход игрока<br>
0: выход из программы<br>
Enter your selection : __<br>

### После ввыода номера команды, будет выведена в консоль дополнительная информация выбранной команды<br>

##пример ввода команды 1<br>
Enter your selection : 1<br>
регистрация игрока, введите Имя, Пароль<br>
p 1<br>
игрок создан : id=1, name=p, account=0<br>

##пример ввода команды 2<br>
Enter your selection : 2<br>
авторизация игрока, введите Имя, Пароль<br>
p 1<br>
ваш token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjk2OTMwNDQ5LCJzdWIiOiJpZD0xLCBuYW1lPXAsIGFjY291bnQ9MCIsImlzcyI6InRlc3QiLCJleHAiOjMzOTM4NjA4OTh9.pvJxpoD7wcZvL8RMh5GZ1Vb_t-d4CLFtGDapkPj5VsM<br>

##пример ввода команды 5<br>
Enter your selection : 5<br>
кредит средств игрока, введите ваш token, значение кредита, номер транзакции<br>
eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjk2OTMwNDQ5LCJzdWIiOiJpZD0xLCBuYW1lPXAsIGFjY291bnQ9MCIsImlzcyI6InRlc3QiLCJleHAiOjMzOTM4NjA4OTh9.pvJxpoD7wcZvL8RMh5GZ1Vb_t-d4CLFtGDapkPj5VsM 100 1<br>
ваш баланс: 100<br>