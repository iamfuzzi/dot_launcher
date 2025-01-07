@echo off
rem устанавливаем размер столбцов, строк
mode con: cols=110 lines=30

setlocal enabledelayedexpansion

rem Устанавливаем переменные для путей
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%source\src
set LIB_DIR=%SRC_DIR%\me\fuzzi\dot\launcher\libraries
set MAIN_CLASS=me.fuzzi.dot.launcher.classes.Main

chcp 866 > nul

rem Компилируем все Java файлы
set JAVA_FILES=
for /r "%SRC_DIR%\me\fuzzi\dot\launcher" %%f in (*.java) do (
    set JAVA_FILES=!JAVA_FILES! "%%f"
)

rem Проверяем, есть ли файлы для компиляции
if "!JAVA_FILES!"=="" (
    echo No Java files found to compile.
    pause
    exit /b 1
)

rem Компилируем Java файлы, перенаправляя вывод в nul
javac -d "%PROJECT_DIR%source\bin" -cp "%LIB_DIR%\*" !JAVA_FILES! > nul 2>&1

rem Проверяем, была ли компиляция успешной
if errorlevel 1 (
    echo Errors during compilation!
    pause
    exit /b 1
)

rem Запускаем программу, устанавливая рабочую директорию на PROJECT_DIR
cd /d "%PROJECT_DIR%source"
java -cp "bin;%LIB_DIR%\*" %MAIN_CLASS%

endlocal