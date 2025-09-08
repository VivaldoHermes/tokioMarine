<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isHome = computed(() => route.name === 'home')
const showHeader = computed(() => !isHome.value) // sem header na Home
</script>

<template>
  <div class="app">
    <header v-if="showHeader" style="padding: 12px; border-bottom: 1px solid #eee">
      <nav style="display: flex; gap: 16px">
        <RouterLink to="/schedule">Schedule Transfer</RouterLink>
        <RouterLink to="/transfers">Transfers</RouterLink>
      </nav>
    </header>

    <main :class="{ 'no-pad': isHome }">
      <RouterView />
    </main>
  </div>
</template>

<style>
.no-pad {
  padding: 0 !important;
}
</style>
