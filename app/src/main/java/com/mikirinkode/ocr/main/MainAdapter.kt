package com.mikirinkode.ocr.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikirinkode.ocr.data.ScanResultEntity
import com.mikirinkode.ocr.databinding.ScanResultItemBinding
import com.mikirinkode.ocr.detail.DetailActivity

class MainAdapter: RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var scanResultList = ArrayList<ScanResultEntity>()

    class ViewHolder(private val binding: ScanResultItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(scanResultEntity: ScanResultEntity){
            binding.apply {
                tvResult.text = scanResultEntity.textResult
                Glide.with(itemView.context)
                    .load(scanResultEntity.image)
                    .into(ivCaptured)
            }
            itemView.setOnClickListener {
                val moveToDetail = Intent(itemView.context, DetailActivity::class.java)
                moveToDetail.putExtra(DetailActivity.EXTRA_ID, scanResultEntity.id)
                itemView.context.startActivity(moveToDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ScanResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resultItem = scanResultList[position]
        holder.bind(resultItem)
    }

    override fun getItemCount(): Int = scanResultList.size

    fun setData(itemList: List<ScanResultEntity>){
        this.scanResultList.clear()
        this.scanResultList.addAll(itemList)
    }
}