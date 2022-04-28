package com.mikirinkode.scanner.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikirinkode.scanner.databinding.ScanResultItemBinding
import com.mikirinkode.scanner.core.domain.model.ScanResult
import com.mikirinkode.scanner.ui.detail.DetailActivity
import com.mikirinkode.scanner.utils.Converter

class MainAdapter: RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var scanResultList = ArrayList<ScanResult>()

    class ViewHolder(private val binding: ScanResultItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(scanResult: ScanResult){
            binding.apply {
                tvResult.text = scanResult.textResult
                val image = Converter.toBitmap(scanResult.image)
                Glide.with(itemView.context)
                    .load(image)
                    .into(ivCaptured)
            }
            itemView.setOnClickListener {
                val moveToDetail = Intent(itemView.context, DetailActivity::class.java)
                moveToDetail.putExtra(DetailActivity.EXTRA_ENTITY, scanResult)
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

    fun setData(itemList: List<ScanResult>){
        this.scanResultList.clear()
        this.scanResultList.addAll(itemList)
    }
}