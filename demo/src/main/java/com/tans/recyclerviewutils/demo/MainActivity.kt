package com.tans.recyclerviewutils.demo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import com.tans.recyclerviewutils.CircleLinearLayoutManager
import com.tans.recyclerviewutils.demo.databinding.HelloWorldItemLayoutBinding
import com.tans.tadapter.spec.SimpleAdapterSpec
import com.tans.tadapter.spec.toAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val testAdapter = SimpleAdapterSpec<TestData, HelloWorldItemLayoutBinding>(
            layoutId = R.layout.hello_world_item_layout,
            bindData = { position, data, binding ->
                binding.data = data
                binding.root.setBackgroundColor(if (position % 2 == 0) Color.rgb(255, 0, 0) else Color.rgb(0, 255, 0))
            },
            dataUpdater = Observable.fromArray(emptyList(), List(30) { TestData(index = it.toString(), title = "Hello,World") })
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        ).toAdapter()
        hello_rv.layoutManager = CircleLinearLayoutManager()
        hello_rv.itemAnimator = DefaultItemAnimator()
        hello_rv.adapter = testAdapter
    }

    data class TestData(val index: String,
                        val title: String)
}
