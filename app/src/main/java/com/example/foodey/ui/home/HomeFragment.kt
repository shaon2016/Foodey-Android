package com.example.foodey.ui.home


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodey.R
import com.example.foodey.models.Food
import com.example.foodey.util.obtainViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment() {
    private lateinit var foodAdapter: FoodRVAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeVM by lazy { obtainViewModel(HomeVM::class.java, viewModelFactory) }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initVar()
        initView()
    }

    private fun initView() {
        rvFoods.layoutManager = GridLayoutManager(context!!, 2)
        rvFoods.adapter = foodAdapter

        homeVM.syncFoods()
        initSearch()

        homeVM.foods.observe(requireActivity(), Observer {
            it?.let {
                foodAdapter.addUniquely(it as java.util.ArrayList<Food>)
            }
        })
    }

    private fun initSearch() {
        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                s?.let {
                    if (foodAdapter.items.isNotEmpty())
                        foodAdapter.filter.filter(s)
                }
            }
        })
    }


    private fun initVar() {
        foodAdapter =
            FoodRVAdapter(context!!, ArrayList())
    }


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
