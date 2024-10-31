package org.fsr.collect.android.database.itemsets

import org.fsr.collect.android.fastexternalitemset.ItemsetDbAdapter
import org.fsr.collect.android.itemsets.FastExternalItemsetsRepository

class DatabaseFastExternalItemsetsRepository : FastExternalItemsetsRepository {

    override fun deleteAllByCsvPath(path: String) {
        ItemsetDbAdapter().open().use {
            it.delete(path)
        }
    }
}
