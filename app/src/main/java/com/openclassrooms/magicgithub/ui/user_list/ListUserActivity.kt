package com.openclassrooms.magicgithub.ui.user_list

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.magicgithub.R
import com.openclassrooms.magicgithub.databinding.ActivityListUserBinding
import com.openclassrooms.magicgithub.di.Injection.getRepository
import com.openclassrooms.magicgithub.model.User

class ListUserActivity : AppCompatActivity(), UserListAdapter.Listener {
    private lateinit var binding: ActivityListUserBinding
    private lateinit var adapter: UserListAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureFab()
        configureRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun configureRecyclerView() {
        adapter = UserListAdapter(this, getRepository())
        binding.activityListUserRv.adapter = adapter

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPos = source.adapterPosition
                val toPos = target.adapterPosition
                adapter.moveItem(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val user = adapter.getItemAt(position)
                user.isActive = !user.isActive
                adapter.notifyItemChanged(position)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                when (actionState) {
                    ItemTouchHelper.ACTION_STATE_DRAG -> {
                        viewHolder?.itemView?.apply {
                            setBackgroundColor(Color.parseColor("#E3F2FD")) // Bleu clair
                            elevation = 8f
                        }
                    }
                    ItemTouchHelper.ACTION_STATE_IDLE -> {
                        viewHolder?.itemView?.apply {
                            elevation = 0f
                            // Restaurer la couleur d'origine en fonction de l'état actif/inactif
                            val user = adapter.getItemAt(viewHolder.adapterPosition)
                            setBackgroundColor(if (user.isActive) Color.WHITE else Color.parseColor("#FFCDD2"))
                        }
                    }
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // Restaurer l'apparence normale quand le drag est terminé
                viewHolder.itemView.apply {
                    elevation = 0f
                    val user = adapter.getItemAt(viewHolder.adapterPosition)
                    setBackgroundColor(if (user.isActive) Color.WHITE else Color.parseColor("#FFCDD2"))
                }
            }
        }

        itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.activityListUserRv)
    }

    private fun configureFab() {
        binding.activityListUserFab.setOnClickListener {
            getRepository().addRandomUser()
            loadData()
        }
    }

    private fun loadData() {
        adapter.updateList(getRepository().getUsers())
    }

    override fun onClickDelete(user: User) {
        Log.d(javaClass.name, "User tries to delete an item.")
        getRepository().deleteUser(user)
        loadData()
    }
}