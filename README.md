## **LogParser**

[![Maintainability](https://api.codeclimate.com/v1/badges/fb1eba8563fae048a411/maintainability)](https://codeclimate.com/github/Denis-Shakhurov/LogParser/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/fb1eba8563fae048a411/test_coverage)](https://codeclimate.com/github/Denis-Shakhurov/LogParser/test_coverage)
___
### Нативное CLI приложение написанное с помощью Picocli для парсера log файлов.
Установка:```make install```

Заруск: ```./build/install/app/bin/app "<param1>" "<param2>"```
* param1 - путь к директории в которой находятся файлы
* param2 - запрос, три вида запроса
  * ```get <field1>```
  * ```get <field1> for <field2> = <value>```
  * ```get <field1> for <field2> = <value> and date between <dateAfter> and <dateBefore>```
где field1, field2 - одно из значений: ip, user, date, event, taskId, status
      value - значение field2
