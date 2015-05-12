# Recycler-Core

Recycler-Core provides a clean MVC framework for managing your RecyclerView Adapters.

## Motivation

Blindly dealing with multiple view types in a `RecyclerView.Adapter` can frequently lead to unmaintainable source code.  The standard pattern of managing multiple views also does not promote reusability between multiple adapters.

Recycler-Core attempts to solve the maintainability and reusability problems that are ran into when dealing with multiple views in a `RecyclerView.Adapter` by creating an MVC pattern for RecyclerViews.

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

Next I create the model.  The model is a standard POJO that must extend `RecyclerCoreModel` which has you implement `buildController`.

The Model + Controller are (justifiably) very coupled, so you'll need to build the controller before jumping into that method.

I'm still going to write it here, but you'll need to jump back up after you've checked out the controller for everything to make sense.

`UserModel.java`:

```java
// ...
public class UserModel extends RecyclerCoreModel {

    public String mName;
    public String mEmail;

    @Override
    public RecyclerCoreController buildController(LayoutInflater inflater, ViewGroup parent, Activity activity)
    {
        View rootView = inflater.inflate(R.layout.element_user, parent, false);
        return new UserController(rootView, activity);
    }
    
    // ... Setters + Getters

}
```

### The Controller

Now we create the controller.

This class must extend `RecyclerCoreController` with the generic of the model that was just created.

The magic happens in the `bind` method, where this object will be passed an instance of the model.

```java
// ...
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

### Registering

Now that we have a fully implemented list item, we can now create an adapter + register the model.

```java
// ...
public class MyAdapter extends RecyclerCoreAdapter {

    public MyAdapter(List<RecyclerCoreModel> modelList, Activity activity)
    {
        super(modelList, activity);
    }

    @Override
    protected void registerModels()
    {
        registerModel(new UserModel()); // <---- Registration
        // Here you would register other elements
    }

}
```

### In use

Now that we have our view registered to the adapter, we can now use it.

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

TestAdapter adapter = new TestAdapter(models, this);
mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
mRecyclerView.setAdapter(adapter);
```

And now you have:

![img](http://i.imgur.com/NP7Wboq.png)

## Extras

### ProgressRecyclerView

RecyclerCore also provides a custom view called `ProgressRecyclerView`.  This is just a `RecyclerView` with a `ProgressBar` inside of a `RelativeLayout` and it manages displaying the `ProgressBar` when the adapter is set.

I pretty much never use RecyclerViews that aren't encapsulated inside this pattern so I decided to include it in this library.

## License

[MIT](license.txt)
