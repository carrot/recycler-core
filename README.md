# Recycler-Core

Recycler-Core provides a clean MVC framework for managing your RecyclerView Adapters.

## Motivation

Blindly dealing with multiple view types in a `RecyclerView.Adapter` can frequently lead to unmaintainable source code.  The standard pattern of managing multiple views also does not promote reusability between multiple adapters.

Recycler-Core attempts to solve the maintainability and reusability problems that are ran into when dealing with multiple views in a `RecyclerView.Adapter` by creating an MVC pattern for RecyclerViews.

Download
--------
Configure the project-level `build.gradle` to include the 'android-apt' plugin
```groovy
buildscript {
  repositories {
    jcenter()
  }
  dependencies {
    ...
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}
```

Then, apply the 'android-apt' plugin in the module-level `build.gradle` and add the Recycler-Core dependencies:

Gradle:
```groovy
apply plugin: 'com.neenbedankt.android-apt'

android {
  ...
}

dependencies {
  compile 'com.carrotcreative.recyclercore:recycler-core:2.0-alpha'
  apt 'com.carrotcreative.recyclercore:recyclercore-compiler:2.0-alpha'
}
```

## Usage

### The View

To create a View Type for your RecyclerView, you're going to have to build out a component for the Model, View, and Controller.

In this example, we will build out an element that will display basic information about a user.

The View is a standard Layout XML file with nothing special about it.

`element_user.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="32dp"
    >

    <TextView
        android:id="@+id/name"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        />

    <TextView
        android:id="@+id/email"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        />

</LinearLayout>
```

### The Model

Next I create the model.  

The model is a standard POJO class, which is responsible for storing all of the data
that defines our views.

When defining the model, we need to declare a class level annotation `InjectController`
and pass in the controller and the layout.

`UserModel.java`:

```java
// ...
@RCModel(controller = UserController.class)
public class UserModel {

    public String mName;
    public String mEmail;
    
    // ... Setters + Getters

}
```

### The Controller

Now we create the controller.

This class must extend `RecyclerCoreController` with the generic of the model that was just created.

The magic happens in the `bind` method, where this object will be passed an instance of the model.

`UserController.java`:

```java
// ...
@RCController(layout = R.layout.element_user_list)
public class UserController extends RecyclerCoreController<UserModel> {

    private TextView mName;
    private TextView mEmail;

    public UserController(View itemView, Activity activity)
    {
        super(itemView, activity);
        mName = (TextView) itemView.findViewById(R.id.name);
        mEmail = (TextView) itemView.findViewById(R.id.email);
    }

    @Override
    public void bind(UserModel model)
    {
        mName.setText(model.getName());
        mEmail.setText(model.getEmail());
    }

}
```

### In use

`MainActivity.java`:

```java
// ...
ArrayList<RecyclerCoreModel> models = new ArrayList<>();

UserModel brandon = new UserModel()
        .setEmail("Brandon@gmail.com")
        .setName("Brandon");
models.add(brandon);

UserModel rob = new UserModel()
        .setEmail("Rob@gmail.com")
        .setName("Rob");
models.add(rob);

RecyclerCoreAdapter adapter = new RecyclerCoreAdapter(models);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
mRecyclerView.setAdapter(adapter);
```

And now you have:

![img](http://i.imgur.com/NP7Wboq.png)

## Extras

### ProgressRecyclerViewLayout

RecyclerCore also provides a custom view called `ProgressRecyclerViewLayout`.  This is just a `RecyclerView` with a `ProgressBar` inside of a `RelativeLayout` and it manages displaying the `ProgressBar` when the adapter is set.

I pretty much never use RecyclerViews that aren't encapsulated inside this pattern so I decided to include it in this library.

### SwipeRefreshProgressRecyclerView

We also have a `SwipeRefreshProgressRecyclerView` that is same as `SwipeRefreshLayout` with `ProgressRecyclerViewLayout` as a child. So it encapsulates the features or `SwipeRefreshLayout`, `RecyclerView` in one View.

### OnLoadPointListener

`ProgressRecyclerViewLayout` also provides us interfaces that can allow us to add unlimited scroll.

```
    /**
     * An interface to add a callback that gets called when the load point is reached.
     * For the load point callback to work, we need to set
     * {@link #setDistanceFromBottomToLoadMore(int)} and the
     * {@link #setOnLoadPointListener(OnLoadPointListener)}
     * <p>
     * This currently only supports {@link android.support.v7.widget.LinearLayoutManager},
     * {@link android.support.v7.widget.StaggeredGridLayoutManager},
     * {@link android.support.v7.widget.GridLayoutManager}
     */
    public interface OnLoadPointListener
    {
        void onReachedLoadPoint();
    }
```

In order to get that working, we need to set `distanceFromBottomToLoadMore` attribute value, which can be set either in the Layout or programmatically.

```
<com.carrotcreative.recyclercore.widget.ProgressRecyclerViewLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/recycler_view_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:distanceFromBottomToLoadMore="0"
    />
```
OR
```
    /**
     * Helper method to set the distance from bottom value programmatically, if its not set in
     * the layout file.
     * <p>
     * A value of 0, means the #onReachedLoadPoint is called when the last child starts becoming
     * visible.
     * <p>
     * A value of 1 means the #onReachedLoadPoint is called when the secnd last child starts
     * becoming visible
     * <p>
     * A Negative value is considered and invalid value
     *
     * @param distanceFromBottomToLoadMore The no of item from the bottom of the recycler view,
     *                                    after which #OnLoadPointListener is called
     */
    public void setDistanceFromBottomToLoadMore(int distanceFromBottomToLoadMore)
```

Once this is done, we need to set a listener that will be called when the recycler view reached the load point
```
        mRecyclerViewLayout.setOnLoadPointListener(new ProgressRecyclerViewLayout
                .OnLoadPointListener()
        {
            @Override
            public void onReachedLoadPoint()
            {
                loadMoreData();
            }
        });
```

## License

[MIT](license.txt)
