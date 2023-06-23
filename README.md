# Puckpaste

Сервис для сохранения отрывков текста и дальнейшего их распространения посредством сгенерированных ссылок
(аналог [Pastebin](https://pastebin.com/)).

# Описание функциональности
Основной сущностью сервиса является пост, который состоит из
- текстового содержимого (например, кода)
- названия
- описания
- времени жизни (после которого пост будет автоматически удален)

Все поля, кроме текстового содержимого являются опциональными.

При создании поста пользователь получает ссылку на него, которой он может делиться с другими пользователями.  
Любой пользователь, имеющий ссылку на пост, может просматривать его.  
После публикации пост не может быть изменен или удален до истечения срока его жизни.   
Таким образом, если ссылка на пост будет утеряна всеми пользователями, то доступ к нему будет невозможен.  

Также сервис предоставляет статистику по текущему количеству постов в системе.

## Дополнительные фичи на будущее
- Продление времени жизни постов 
- Сохранение ссылок на недавно загруженные посты в сессии
- Получение списка постов (с возможностью сортировки и пагинации)
- Поиск постов по полям
- Дополнительные опциональные поля
  - Имя автора
  - Теги
- Авторизация, дающая возможность удаления и редактирования постов

# REST API

---
### POST /api/posts
Создание нового поста.

Поля запроса:
- **title** - название поста (не более 100 символов, по умолчанию генерируется автоматически, опциональное поле)
- **description** - описание поста (не более 1500 символов, по умолчанию - пусто, опциональное поле)
- **content** - текстовый фрагмент (не более 20000 символов, обязательное поле)
- **expirationTimeInDays** - время жизни поста в днях (от 1 до 30, по умолчанию - 7, опциональное поле)

Пример запроса:
```json
{
  "title": "Post title",
  "description": "Some description",
  "content": "Some meaningful (not sure) code",
  "expirationTimeInDays": 10
}
```

Поля ответа:
- **id** - идентификатор поста
- **title** - название поста
- **description** - описание поста
- **content** - текстовое содержимое
- **expiresAt** - время истечения срока жизни поста

Ссылка на созданный пост располагается в заголовке `Location`.

HTTP коды ответов:
- 201 - ОК
- 400 - отсутствует хотя бы одно из обязательных полей / нарушено хотя бы одно из ограничений / некорректный формат json

Пример ответа:
```json
{
  "id": "53",
  "title": "Post title",
  "description": "Some description",
  "content": "Some meaningful (not sure) code",
  "expires": "2023-07-22 11:04:29"
}
```

---
### GET /api/posts/{id}
Получение поста по идентификатору.

Поля ответа:
- **id** - идентификатор поста
- **title** - название поста
- **description** - описание поста
- **content** - текстовое содержимое
- **expiresAt** - время истечения срока жизни поста

HTTP коды ответов:
- 200 - ОК
- 404 - пост не был найден (либо несуществующий `id`, либо срок жизни поста истек)

Пример ответа:
```json
{
  "id": "53",
  "title": "Post title",
  "description": "Some description",
  "content": "Some meaningful (not sure) code",
  "expires": "2023-07-22 11:04:29"
}
```

---
### GET /api/stats
Получения текущего количества постов в системе.

Поля ответа:
- postTotalCount - суммарное количество постов в системе

HTTP коды ответов:
- 200 - ОК

Пример ответа:
```json
{
  "postTotalCount": 132
}
```

---
## Общие замечания
В случае возникновения ошибки на стороне сервера все методы возвращают код 500.

В случае неудачного ответа c кодами 4xx его тело имеет следующий вид:
```json
{
  "message": "error message"
}
```
