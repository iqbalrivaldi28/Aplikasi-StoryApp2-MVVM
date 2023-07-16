package com.example.intermediateiqbal.paging

import androidx.paging.ItemKeyedDataSource
import androidx.paging.PagingState
import com.example.intermediateiqbal.retrofit.APIService
import com.example.intermediateiqbal.retrofit.response.StoryItem

class PagingSource(
    private val apiService: APIService,
    private val token: String) : PagingSource<Int, StoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: ItemKeyedDataSource.LoadParams<Int>): androidx.paging.PagingSource.LoadResult<Int, StoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = APIService.getAllStoriesPagingData(position, params.loadSize, "Bearer $token")
            androidx.paging.PagingSource.LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return androidx.paging.PagingSource.LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}