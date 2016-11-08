# News Feed Feature
Reads the top stories api and display top stories.

## Implementation details
The whole activity is implmented using a single `SuperRecyclerView`, inflating `view_news.xml`.

The activity starts by subscribing to the presenter and the presenter populates the adapter with the results.

Pagination is triggered when the user scroll to the last 10 items. This is acheived using `SuperRecyclerView.setOnMoreListener()`, calling the presenter for more data. 

When the data runs out, `SuperRecyclerView.setOnMoreListener()` is removed to prevent further paging.

## Subscription to Data Stream
`Activity.OnResume()` is overridden and subscribes to the presenter for data updates.

`Activity.OnPause()` is overridden and unsubscribes from network calls when called to prevent memory leaks.

## Pull to refresh
`SwipeRefreshLayout.setRefreshListener()` is subscribed to and does a force update via the presenter. This resets the recyclerView and paging is subscribed to again.

## Orientation change
`Activity.onSaveInstanceState` is overridden and stores the state of `RecyclerView.LayoutManager` and `RecyclerView.Adapter`

`Activity.onRestoreInstanceState` is overriden and the state is retreived. The state is restored on `Activity.onResume`
