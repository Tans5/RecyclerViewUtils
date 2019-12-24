package com.tans.recyclerviewutils.demo

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.tans.recyclerviewutils.IgnoreGridLastLineHorizontalDividerController
import com.tans.recyclerviewutils.IgnoreGridLastRowVerticalDividerController
import com.tans.recyclerviewutils.MarginDividerItemDecoration
import com.tans.recyclerviewutils.demo.databinding.HelloWorldItemLayoutBinding
import com.tans.tadapter.spec.SimpleAdapterSpec
import com.tans.tadapter.spec.toAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_grid.*
import java.util.concurrent.TimeUnit

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-23
 */
class GridActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)
        val testAdapter = SimpleAdapterSpec<MainActivity.TestData, HelloWorldItemLayoutBinding>(
            layoutId = R.layout.hello_world_item_layout,
            bindData = { position, data, binding ->
                binding.data = data
                // binding.root.setBackgroundColor(if (position % 2 == 0) Color.rgb(255, 0, 0) else Color.rgb(0, 255, 0))
            },
            dataUpdater = Observable.fromArray(
                emptyList(),
                List(30) { MainActivity.TestData(index = it.toString(), title = "Hello") })
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        ).toAdapter()
        grid_hello_rv.apply {
            layoutManager = GridLayoutManager(this@GridActivity, 4)
            adapter = testAdapter
            addItemDecoration(
                MarginDividerItemDecoration.Companion.Builder()
                    .divider(
                        MarginDividerItemDecoration.Companion.ColorDivider(
                            color = Color.RED,
                            size = 4
                        )
                    )
                    .dividerDirection(MarginDividerItemDecoration.Companion.DividerDirection.Horizontal)
                    .dividerController(IgnoreGridLastLineHorizontalDividerController(4))
                    .build()
            )
            addItemDecoration(
                MarginDividerItemDecoration.Companion.Builder()
                    .divider(
                        MarginDividerItemDecoration.Companion.ColorDivider(
                            color = Color.RED,
                            size = 4
                        )
                    )
                    .dividerDirection(MarginDividerItemDecoration.Companion.DividerDirection.Vertical)
                    .dividerController(IgnoreGridLastRowVerticalDividerController(4))
                    .build()
            )

        }
    }

}