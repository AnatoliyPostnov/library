Задание Focus Start: Реализация ORM для работника библиотеки.

Программа обладает следующей функциональностью:

1. Добавление книг в базу данных.
2. Поиск книг по разным условиям.
3. Удаление книг по разным условиям.
4. Поиск авторов всех книг.
5. Добавление пользователей в базу данных.
6. Возможность взять книгу пользователем.
7. Возможность сдать книгу пользователю.
8. Контроль за книгами:
    а) можно получить все книги, которые были взяты пользователями.
    б) можно получить не сданные книги по конкретному пользователю.
    в) в программе постоянно крутится Scheduled, который отслеживает все книги, которые были
    взяты более чем один месяц назад и отправляет пользователям извещение о том, что книги нужно сдать.
9. В программе показана возможность расширения программы двумя классами genre и theme.
   Их польза в том, что можно добавить функционал по поиску книг по конкретным жанрам и
   темам, и возможен другой функционал.


10 Для взаимодействия с ORM доступен следующий интерфейс:


1. Добавить одну книгу: 
	метод - POST; 
	адрес - http://localhost:8080/add/book; 
	пример Body - 
{
    "name": "Spring 5 для профессионалов",
    "volume": "1120",
    "dateOfPublishing": "2019-11-05",
    "dateOfWriting": "2018-05-05",
    "authors": [
        {
            "name": "Юлиана",
            "surname": "Кузьмина",
            "birthday": "1964-06-15"
        },
        {
            "name": "Роб",
            "surname": "Харроп",
            "birthday": "1964-06-15"
        },
        {
            "name": "Крис",
            "surname": "Шедер",
            "birthday": "1964-06-15"
        }
    ]
}

2. Добавить несколько книг:
	метод - POST; 
	адрес - http://localhost:8080/add/books; 
	пример Body -
[
	{
	    "name": "55 устных тем по английскому языку",
	    "volume": "155",
	    "dateOfPublishing": "2003-11-05",
	    "dateOfWriting": "2002-05-05",
	    "authors": [
	        {
	            "name": "Татьяна",
	            "surname": "Журина",
	            "birthday": "1996-06-17"
	        },
	        {
	            "name": "Неизвестный",
	            "surname": "Автор",
	            "birthday": "1999-06-17"
	        }
	    ]
	},
	{
	    "name": "Java. Полное руководство",
	    "volume": "1486",
	    "dateOfPublishing": "2019-10-16",
	    "dateOfWriting": "2002-05-05",
	    "authors": [
	        {
	            "name": "Герберт",
	            "surname": "Шилдт",
	            "birthday": "1977-06-17"
	        }
	    ]
	},
	{
	    "name": "Неизвестная книга",
	    "volume": "10",
	    "dateOfPublishing": "2019-10-16",
	    "dateOfWriting": "2002-05-05",
	    "authors": [
	        {
	            "name": "Неизвестный",
	            "surname": "Автор",
	            "birthday": "1999-06-17"
	        }
	    ]
	}
]

3. Получить все книги
	метод - GET; 
	адрес - http://localhost:8080/get/books; 
	Body - пусто

4. Найти книги по автору
	метод - GET;
	адрес - http://localhost:8080/find/books/by/author;
	Пример Body:
{
	"name": "Неизвестный",
	"surname": "Автор",
	"birthday": "1999-06-17"
}

5. Найти книги по имени и фамилии автора
	метод GET;
	адрес - http://localhost:8080/find/books/by/author/name/and/surname?name={Имя автора}&surname={Фамилия автора}
	Body пуст

6. Найти книги по названию книги
    метод GET
    адрес - http://localhost:8080/find/books/by/books/name?name={название книги}
    Body пуст

7. Удалить книгу
	метод DELETE;
	адрес - http://localhost:8080/delete/book;
	пример Body:
{
    "name": "Spring 5 для профессионалов",
    "volume": "1120",
    "dateOfPublishing": "2019-11-05",
    "dateOfWriting": "2018-05-05",
    "authors": [
        {
            "name": "Юлиана",
            "surname": "Кузьмина",
            "birthday": "1964-06-15"
        },
        {
            "name": "Роб",
            "surname": "Харроп",
            "birthday": "1964-06-15"
        },
        {
            "name": "Крис",
            "surname": "Шедер",
            "birthday": "1964-06-15"
        }
    ]
}

8. Получить всех авторов
	метод - GET
	адрес - http://localhost:8080/get/authors
	Body пуст.

9. Удалить автора и все книги с этим автором
	метод - DELETE
	адрес - http://localhost:8080/delete/author
	Пример Body:
{
	"name": "Неизвестный",
	"surname": "Автор",
	"birthday": "1999-06-17"
}

10. Получить все книги, которые в данный момент находятся у пользователей
    метод - GET
    адрес - http://localhost:8080/get/received/books
    Body пуст.

11. Добавить читательский билет
    метод - POST
    адрес - http://localhost:8080/add/libraryCard
    пример Body:
{
"client":
	{
	    "phone": "89533576500",
	    "email": "postnov-90@mail.ru",
	    "passport":
	        {
	            "name": "Петя",
	            "surname": "Бубликов",
	            "birthday": "1964-06-15",
	            "number": "4567",
	            "series": "1553445",
	            "authorityIssuer": "Piter",
	            "dateSigning": "1990-05-05"
	        }
	}
}

12. Получить все зарегистрированные читательские билеты
    метод - GET
    адрес - http://localhost:8080/get/received/books
    Body пуст.

13. Найти конкретный читательский билет по номеру и серии паспорта пользователя
    метод - GET
    адрес - http://localhost:8080/get/libraryCard?number={номер паспорта}&series={номер серии паспорта}
    Body пуст.

14. Дать книжку пользователю
    метод POST
    адрес - http://localhost:8080/received/book/by/book?number={номер паспорта}&series={номер серии паспорта}
    Пример Body:
{
    "name": "Spring 5 для профессионалов",
    "volume": "1120",
    "dateOfPublishing": "2019-11-05",
    "dateOfWriting": "2018-05-05",
    "authors": [
        {
            "name": "Юлиана",
            "surname": "Кузьмина",
            "birthday": "1964-15-06"
        },
        {
            "name": "Роб",
            "surname": "Харроп",
            "birthday": "1964-06-15"
        },
        {
            "name": "Крис",
            "surname": "Шедер",
            "birthday": "1964-06-15"
        }
    ]
}

15. Вернуть книжку пользователем по названию книжки
    метод - POST
    адрес - http://localhost:8080/return/books/by/book/name?number={номер паспорта пользователя}&series={номер серии пользователя}&name={Название книжки}
    Body пуст.

16. Получить все книжки взятые пользователем по номеру и серии паспорта пользователя.
    метод - GET
    адрес - http://localhost:8080/get/received/books/by/passportS/number/and/series?number={номер паспорта}&series={серия паспорта}
    Body пуст.

17. Получить всю историю взятия книжек конкретным пользователем.
    метод - GET
    адре - http://localhost:8080/get/history/received/books/by/passportS/number/and/series?number={номер паспорта}&series={серия паспорта}
    Body пуст.

18. Получить вообще все книжки которые в данный момент находятся у пользователей.
    метод - GET
    адрес - http://localhost:8080/get/all/received/books
    Body пуст.

Для тестирования проекта через postman необходимо установить в Headers: Content-Type - application/json;charset=UTF-8

Для запуска проекта необходимо запустить файл applicationLibrary
