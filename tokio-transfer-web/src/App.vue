<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isHome = computed(() => route.name === 'home')
</script>

<template>
  <div class="app">
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
