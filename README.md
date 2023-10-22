## training_ylab<br>
### для сборки проекта нужно сменить текущую директорию на директорию проекта и написать в консоли: <br>
mvn compile
###чтобы запустить скомпилированный код, нужно в консоли из этой же директории написать:<br>
### после запуска программы в косоль будут выведены доступные команды<br>
mvn exec:java -Dexec.mainClass="Main"
### пример<br>
####MENU<br>
1: registration player<br>
2: authorization player<br> 
3: current account of the player<br>
4: debit operation of the player<br>
5: credit operation of the player<br>
6: listing of the history debit/credit operation of the player<br>
7: audit of the player<br>
8: exit of authorization<br>
0: exit of program<br>
Enter your selection : __<br>

### После ввыода номера команды, будет выведена в консоль дополнительная информация выбранной команды<br>

##пример ввода команды 1<br>
Enter your selection : 1<br>
registration player, input Name, Password
p 1<br>
player created : id=1, name=p, account=0<br>

##пример ввода команды 2<br>
Enter your selection : 2<br>
authorization player, input Name, Password<br>
p 1<br>
your token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjk2OTMwNDQ5LCJzdWIiOiJpZD0xLCBuYW1lPXAsIGFjY291bnQ9MCIsImlzcyI6InRlc3QiLCJleHAiOjMzOTM4NjA4OTh9.pvJxpoD7wcZvL8RMh5GZ1Vb_t-d4CLFtGDapkPj5VsM<br>

##пример ввода команды 5<br>
Enter your selection : 5<br>
credit operation of the player, input your token, value of debit, ID transaction<br>
eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwiaWF0IjoxNjk2OTMwNDQ5LCJzdWIiOiJpZD0xLCBuYW1lPXAsIGFjY291bnQ9MCIsImlzcyI6InRlc3QiLCJleHAiOjMzOTM4NjA4OTh9.pvJxpoD7wcZvL8RMh5GZ1Vb_t-d4CLFtGDapkPj5VsM 100 1<br>
your current account: 100<br>