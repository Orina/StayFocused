This is advanced tasks list android app for codepath.com

Task entry has following fields:
- title
- comments
- completed status
- due date
- priority (High or Normal)

User can:
- create a new task
- update an existent task
- change task completion status (active or archived)
- delete a task

Technical details:

* Implemented Model-View-Presenter design pattern.
* RecyclerView with StaggeredGridLayoutManager from support library was used for tasks display.
* Navigation drawer was used for main app menu.
* Implemented shared element activity transitions. 
* RecyclerView was decorated with swipe-to-dismiss to complete an active task complete or delete an archived one.
* TextInputLayout with default animation was used for editing task title and description.
* DatePickerDialog was used for due date selection.

Persistence: data are stored in Sqlite database.

<br/>

<img src="https://github.com/Orina/StayFocused/blob/master/advancedTaskList-22.gif" />
