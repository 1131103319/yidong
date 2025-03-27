import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state: {
    uploadHistory: [],
    statistics: null,
    loading: false,
    error: null
  },
  mutations: {
    setUploadHistory(state, history) {
      state.uploadHistory = history
    },
    setStatistics(state, stats) {
      state.statistics = stats
    },
    setLoading(state, loading) {
      state.loading = loading
    },
    setError(state, error) {
      state.error = error
    }
  },
  actions: {
    async fetchUploadHistory({ commit }) {
      try {
        commit('setLoading', true)
        const response = await axios.get('/api/uploads')
        commit('setUploadHistory', response.data)
      } catch (error) {
        commit('setError', error.message)
      } finally {
        commit('setLoading', false)
      }
    },
    async uploadFile({ commit }, file) {
      try {
        commit('setLoading', true)
        const formData = new FormData()
        formData.append('file', file)
        const response = await axios.post('/api/upload', formData)
        return response.data
      } catch (error) {
        commit('setError', error.message)
        throw error
      } finally {
        commit('setLoading', false)
      }
    }
  },
  getters: {
    getUploadHistory: state => state.uploadHistory,
    getStatistics: state => state.statistics,
    isLoading: state => state.loading,
    getError: state => state.error
  }
})