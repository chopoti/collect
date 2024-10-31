package org.fsr.collect.android.formlists.savedformlist

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import org.fsr.collect.android.R
import org.fsr.collect.forms.instances.Instance
import org.fsr.collect.lists.RecyclerViewUtils.matchParentWidth
import org.fsr.collect.lists.selects.MultiSelectAdapter

class SelectableSavedFormListItemViewHolder(parent: ViewGroup) :
    MultiSelectAdapter.ViewHolder<Instance>(
        SavedFormListItemView(parent.context)
    ) {
    private var selectView = itemView

    init {
        matchParentWidth()
    }

    override fun setItem(item: Instance) {
        (itemView as SavedFormListItemView).setItem(item)
    }

    override fun getCheckbox(): CheckBox {
        return (itemView as SavedFormListItemView).binding.checkbox
    }

    override fun getSelectArea(): View {
        return selectView
    }

    fun setOnDetailsClickListener(listener: () -> Unit) {
        selectView = itemView.findViewById(R.id.selectView)
        selectView.setOnClickListener { listener() }
    }
}
