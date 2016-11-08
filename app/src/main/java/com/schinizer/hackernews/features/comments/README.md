# Comments Feature
Opens up the story and display its comments. User can click on the story to view the actual article.

Supports on demand loading as a solution to scalibility.

## Implementation details
For performance reasons, the activity is implemented using a single `MATCH_PARENT` `RecyclerView`

The activity launches and fetch the `Item` data from the intent, then it populated the `RecyclerView` with itself and its comments.

## Nested Comments
This is acheived using a `WRAP_CONTENT` `RecyclerView`. The `RecyclerView` is populated when `X Comments` is clicked on.

## On demand loading
The comment is only subscribed `onBindViewHolder()` and unsubscribes `onViewDetachedFromWindow()`

## Pull to refresh
`SwipeRefreshLayout.setRefreshListener()` is subscribed to and does a force update via the presenter.

The childs are notified and flagged for network refresh.

## Launching Article
The story's URL is launched via a `ACTION_VIEW` intent and allows the user to choose their preferred viewing method.
