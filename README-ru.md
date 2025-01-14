> ## Инструкция по установке и использованию
> Первым делом, нужно убедиться, что у вас установлена Java. Рекомендуется устанавливать последнюю версию. Это нужно для того, чтобы сразу компилировать программу, т.к. скачивая архив вы скачиваете исходный код, а .bat файлы для запуска сразу компилируют программу.
> 
> Убедившись, что у Вас есть Java, начнем установку.
> 
> - Скачать весь исходный код (Главная страница (Code) > Code > Download ZIP)
> - Распаковать архив в любое удобное место
> - Открыть run_main.bat[*](#что-делать-в-случае-если-программа-не-работает-или-работает-плохо) (если у вас Windows 10 - используйте 1 версию, если Windows 11 - 2)
>
> Если Вы хотите использовать программу через встроенную консоль, то следует написать эти комманды для создания первой сборки:
> ```java
> jdk 
> download -1.21.1 -"Новая сборка"
> fabric -0.16.9 -"Новая сборка"
> libraries -"Новая сборка"
> assets -"Новая сборка"
> launch -"Новая сборка"
> ```
> Либо же запустите скрипт, который делает тоже самое.
> ```java
> script -new --c
> ```
> 
> Для дальнейших действий в консоли см. раздел [консольных команд](#консольы).

> ## Консоль
> Данный раздел создан для того, чтобы ознакомиться с тем, как использовать лаунчер через консоль, как писать свои скрипты.
>
> Для начала нужно разобраться как запускать какие-либо команды.\
> Любая команда выглядит примерно так:
> 
> `название -аргумент1 -"аргумент номер 2"...`
> 
> Аргументы обязательно передаются с символом **-**. Аргумент можно передавать как просто без кавычек, так и с ними, если нужно передать аргумент, в котором более 1 слова. Аргументы разделяются друг от друга через пробел, поэтому не нужно ставить аргументы вида 1.20.1 в кавычки.
> > ### Список консольных команд
> > > ##### Основные команды
> > > - debug - отображение информации из настроек лаунчера, рабочих путей, методов. Нужен для поиска неисправностей
> > > - [exit, quit, close, stop] - выход из программы
> > 
> > > ##### Команды работы с скриптами
> > > - [script, scr] -path --c - запускает скрипт с нужным именем (всегда в папка\scripts\имя.dtl) (--c означает, что это пользовательский скрипт, системные скрипты - --s, не советуется использовать)
> > > - [console, cons, print] -text - выводит текст в консоль
> > > - [thread, sleep, wait] -secons - Останавливает выполнение скрипта на указанное время
> > > - [force, fstop, interrupt, istop] - принудительно завершает выполнение скрипта
> > > - get --type -arg1 -arg2 - приостанавливает выполнение программы и получает ввод пользователя. Подробнее в (тут будет ссылка)
> >
> > > ##### Команды работы со сборками
> > > - download -version -name. Скачивает нужную версию игры и сохраняет ее с нужным именем
> > > - libraries -name - скачивает все нужные библиотеки для нужной версии игры
> > > - natives -name - скачивает нативы
> > > - assets -name - скачивает нужные ассеты для версии (иконка, панорама главного меню, все звуки и пр.). Внимание! Если Вы ждете несколько минут, а ответа от программы нет, подождите еще, эта команда может долго выполняться
> > > - [run, launch, play, start] -name - запускает нужную сборку
> > 
> > > ##### Прочие команды
> > > - jdk - устанавливает Java для работы с Minecraft
> > > - [config, conf, cnf, param] -parameter -value - устанавливает значение параметра в конфиге лаунчера
> ## Что делать в случае, если программа не работает или работает плохо?
> Если Вы столкнулись с какой-либо ошибкой, сначала попробуйте все альтернативные лоадеры.
> 
> > ### Незначительные ошибки (Не влияющие на работу)
> > - Ошибки компиляции, содержание которых похоже на "URL url = new URL();". (Появляются в debug-лоадерах)