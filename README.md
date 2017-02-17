This is advanced tasks list android app for codepath.com

Task entry has following fields:
- title
- comments
- completed status
- due date
- priority (High or Normal)

User can:
- create a new task
- update existent task
- change task completion status (active or archived)
- delete task

Technical details:

It was attempt to implement Model-View-Presenter design pattern.
For task displaying the RecyclerView from support library was used with StaggeredGridLayoutManager
Use a navigation drawer
use shared element activity transitions 
decorate RecyclerView with swipe-to-dismiss for make an active  task complete or delete archived tasks
use TextInputLayout with default animation for editing task title and description
use DatePickerDialog for due date selection

Persistence: data are stored in Sqlite database.
