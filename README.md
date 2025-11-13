# Sport Club Android App

Приложение для управления участниками спортивного клуба. Использует SQLite через ContentProvider.

---

## Особенности

- Просмотр списка участников (`ListView` + `CursorAdapter`)
- Добавление, редактирование и удаление участников
- Выбор пола через Spinner
- Асинхронная загрузка данных с LoaderManager

---

## Структура

- `MainActivity.kt` – список участников
- `AddMemberActivity.kt` – добавление/редактирование
- `MemberCursorAdapter.kt` – адаптер для списка
- `DatabaseHandler.kt` – работа с SQLite
- `SportclubContract.kt` – описание таблиц
- `SportclubContentProvider.kt` – ContentProvider для CRUD

---

## Таблица members

| Колонка       | Тип       |
|---------------|-----------|
| _id           | INTEGER   |
| first_name    | TEXT      |
| last_name     | TEXT      |
| gender        | INTEGER   |
| sport         | TEXT      |

---

## ContentProvider

- URI: `content://ru.zaikin.sportclub/members`
- Поддержка: query, insert, update, delete
