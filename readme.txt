Реализация backend-a библиотеки:

Для взаимодействия с микросервисом доступен слудующий интерфейс:

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

5. Найти книги по имени и фалмилии автора
	метод GET;
	адрес - http://localhost:8080/find/books/by/author/name/and/surname?name={Имя автора}&surname={Фамилия автора}
	Body пуст

6. Удалить книгу
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

7. Получить всех авторов
	метод - GET
	адрес - http://localhost:8080/get/authors
	Body пуст.

8. Удалить автора и все книги с этим атором
	метод - DELETE
	адрес - http://localhost:8080/delete/author
	Пример Body:
{
	"name": "Неизвестный",
	"surname": "Автор",
	"birthday": "1999-06-17"
}
