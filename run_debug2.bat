@echo off
setlocal enabledelayedexpansion

rem Переменные путей
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%source\src
set LIB_DIR=%SRC_DIR%\me\fuzzi\dot\launcher\libraries
set MAIN_CLASS=me.fuzzi.dot.launcher.classes.Main

chcp 866

rem Компиляция
set JAVA_FILES=
for /r "%SRC_DIR%\me\fuzzi\dot\launcher" %%f in (*.java) do (
    set JAVA_FILES=!JAVA_FILES! "%%f"
)

rem Проверка существования файлов
if "!JAVA_FILES!"=="" (
    chcp 65001
    echo Возникли проблемы с путем к файлам.
    pause
    exit /b 1
)

rem Компиляция с выводом хода компиляции в nul
javac -Xlint:deprecation -encoding UTF-8 -d "%PROJECT_DIR%source\bin" -cp "%LIB_DIR%\*" !JAVA_FILES!

rem Если есть ошибки
if errorlevel 1 (
    chcp 65001
    echo Во время компиляции возникли ошибки! Попробуйте запустить debug-лоадер.
    pause
    exit /b 1
)

rem Запускаем программу, устанавливая рабочую директорию на PROJECT_DIR
cd /d "%PROJECT_DIR%source"
java -cp "bin;%LIB_DIR%\*" %MAIN_CLASS%

endlocal

pause