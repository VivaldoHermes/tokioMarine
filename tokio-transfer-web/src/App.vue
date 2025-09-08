<template>
  <div class="app">
    <header style="padding: 12px; border-bottom: 1px solid #eee">
      <nav style="display: flex; gap: 16px">
        <RouterLink to="/schedule">Schedule Transfer</RouterLink>
        <RouterLink to="/transfers">Transfers</RouterLink>
      </nav>
    </header>
    <main style="padding: 16px">
      <RouterView />
    </main>
  </div>
</template>
