
## Электронный учебно-методический комплекс Р-423 для платформы Android

### 1. Книги и схемы

  Программа поддерживает отображение любых документов **оформленных с помощью языка разметки HTML и имеющих название index.htm** (Microsoft Office Word, Microsoft Office Visio позволяют сохранять и редактировать такие документы.)

#### Файловая структура 
      // Файловая структура библиотеки книг. Корень - assets
      books/
              НАЗВАНИЕ_КНИГИ_1/
                              НАЗВАНИЕ_ГЛАВЫ_1/
                                              index.files
                                              index.htm
                              НАЗВАНИЕ_ГЛАВЫ_2/
                                              index.files
                                              index.htm
              НАЗВАНИЕ_КНИГИ_2/
                              index.files
                              index.htm
              ...

- Каждой книге соответствует папка в books/

- Если в книге есть разделение на главы, то каждой главе соответствует папка с именем "НАЗВАНИЕ_ГЛАВЫ", внутри папки должен лежать документ с именем index.htm и любые необходимые для работы index.htm папки и файлы

- Если деления на главы нет, то достаточно просто папки "НАЗВАНИЕ_КНИГИ", внутри которой есть index.htm

### Галерея

В галерею можно помещать фотографии, группируя их в смысловые разделы, к каждой фотографuu можно добавuть развернутое описание</p>

#### Файловая структура галереи

      // Файловая структура галереи. Корень - assets

      gallery/
              descriptions.json
              НАЗВАНИЕ_РАЗДЕЛА_1/
                              название_фото1.jpg
                              название_фото2.jpg
              НАЗВАНИЕ_РАЗДЕЛА_2/
                              название_фото1.jpg
                              название_фото2.jpg
              ...


- Разделов и фото может быть сколько угодно, но обязательно должен быть хотя бы **1 раздел**, в нем хотя бы **1 фотo**
- Название раздела и название фотографии отображаются в заголовке при просмотре

- к фотографии можно добавить развернутое текстовое описание, поместив его в файл _descriptions.json_

      // Файл descriptions.json
 
      {
        "НАЗВАНИЕ_РАЗДЕЛА_1/название_фото1": "Длинное и содержательное описание",
        ...
        "НАЗВАНИЕ_РАЗДЕЛА_3/название_фото45": "Тоже длинное и содержательное описание"
      }

- Описания фото хранятся  в файле descriptions.json со всеми требованиями формата JSON

- Ключом является "НАЗВАНИЕ_РАЗДЕЛА_1/название_фото1"  (**Без расширения файла!**)

- Программа сравнивает ключи и путь к фото без учета регистра

- Значение - строка любой длины

- Если описания к какой-то фотографии не будет, программа будет работоспособна, просто не покажет описание для этой фотографии