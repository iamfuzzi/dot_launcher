@echo off
setlocal enabledelayedexpansion

rem Сообщение о запуске
chcp 65001 > nul
echo Запсук лаунчера...

rem Путь до текущего файла
set "current_path=%~dp0"
set "current_file=%~n0"

rem Удаление лишних слов из названия файла
set "current_file=!current_file:run_=!"
set "current_file=!current_file:.bat=!"

rem Получение текущей даты и времени
for /f "tokens=1 delims=." %%a in ('wmic os get localdatetime ^| findstr /r "^[0-9]"') do (
    set "datetime=%%a"
)

rem Извлечение даты и времени
set "current_date=!datetime:~6,2!.!datetime:~4,2!.!datetime:~0,4!"  rem ДД.ММ.ГГГГ
set "current_time=!datetime:~8,2!.!datetime:~10,2!.!datetime:~12,2!"  rem ЧЧ.ММ.СС

rem Переменная до файла логов
set "log_file=!current_path!launcher\logs\!current_file!-!current_date!-!current_time!.txt"

rem Переменные путей
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%source\src
set LIB_DIR=%SRC_DIR%\me\fuzzi\dot\launcher\libraries
set MAIN_CLASS=me.fuzzi.dot.launcher.classes.Main

chcp 866 > nul

rem Компиляция
set JAVA_FILES=
for /r "%SRC_DIR%\me\fuzzi\dot\launcher" %%f in (*.java) do (
    set JAVA_FILES=!JAVA_FILES! "%%f"
)

rem Проверка существования файлов
if "!JAVA_FILES!"=="" (
    chcp 65001 > nul
    echo Возникли проблемы с путем к файлам.
    pause
    exit /b 1
)

rem Компиляция с выводом хода компиляции в nul
javac -d "%PROJECT_DIR%source\bin" -cp "%LIB_DIR%\*" !JAVA_FILES! > nul 2>&1

rem Если есть ошибки
if errorlevel 1 (
    chcp 65001 > nul
    echo Во время компиляции возникли ошибки! Попробуйте запустить debug-лоадер.
    pause
    exit /b 1
)

rem Запускаем программу, устанавливая рабочую директорию на PROJECT_DIR
cd /d "%PROJECT_DIR%source"
java -cp "bin;%LIB_DIR%\*" %MAIN_CLASS%

endlocal