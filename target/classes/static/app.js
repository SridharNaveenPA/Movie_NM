const API = {
  movies: '/api/movies',
  auth: {
    login: '/api/auth/login',
    register: '/api/auth/register',
  },
  users: {
    me: '/api/users/me',
  },
  reviews: '/api/reviews',
};

function token() { return localStorage.getItem('token'); }
function setToken(t){ localStorage.setItem('token', t); }
function clearToken(){ localStorage.removeItem('token'); }

function authHeaders(){
  const t = token();
  return t ? { 'Authorization': 'Bearer ' + t } : {};
}

function qs(sel){ return document.querySelector(sel); }

function updateNav(){
  const t = token();
  const login = qs('#nav-login');
  const register = qs('#nav-register');
  const profile = qs('#nav-profile');
  const logout = qs('#nav-logout');
  if (login) login.classList.toggle('hidden', !!t);
  if (register) register.classList.toggle('hidden', !!t);
  if (profile) profile.classList.toggle('hidden', !t);
  if (logout) logout.classList.toggle('hidden', !t);
  if (logout) logout.onclick = () => { clearToken(); location.href = '/index.html'; };
}

async function fetchJSON(url, opts={}){
  const res = await fetch(url, {
    ...opts,
    headers: { 'Content-Type': 'application/json', ...(opts.headers||{}) },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.headers.get('content-type')?.includes('application/json') ? res.json() : res.text();
}

function movieCard(m){
  return `
    <div class="card movie-card">
      <img src="${m.poster_url || 'https://picsum.photos/400/600?random='+m.movie_id}" alt="${m.title}">
      <h4>${m.title} (${m.release_year || 'N/A'})</h4>
      <div class="muted">${m.genre || 'N/A'}</div>
      <div class="muted">Dir: ${m.director || 'N/A'}</div>
      <div>⭐ ${m.average_rating?.toFixed(1) ?? 'No ratings'}</div>
      <a href="/movie.html?id=${m.movie_id}"><button>View</button></a>
    </div>
  `;
}

async function loadMovies(){
  const grid = qs('#movies'); if (!grid) return;
  const q = qs('#searchQuery').value.trim();
  const genre = qs('#searchGenre').value.trim();
  const year = qs('#searchYear').value.trim();
  const params = new URLSearchParams();
  if (q) params.set('q', q);
  if (genre) params.set('genre', genre);
  if (year) params.set('year', year);
  const data = await fetchJSON(`${API.movies}?${params.toString()}`);
  grid.innerHTML = data.content.map(movieCard).join('');
}

async function handleLogin(){
  const form = qs('#loginForm'); if (!form) return;
  form.onsubmit = async (e)=>{
    e.preventDefault();
    const payload = { username: qs('#loginUsername').value, password: qs('#loginPassword').value };
    try{
      const res = await fetchJSON(API.auth.login, { method:'POST', body: JSON.stringify(payload) });
      setToken(res.token);
      location.href = '/index.html';
    }catch(err){ alert(err.message); }
  };
}

async function handleRegister(){
  const form = qs('#registerForm'); if (!form) return;
  form.onsubmit = async (e)=>{
    e.preventDefault();
    const payload = { username: qs('#regUsername').value, email: qs('#regEmail').value, password: qs('#regPassword').value };
    try{
      const res = await fetchJSON(API.auth.register, { method:'POST', body: JSON.stringify(payload) });
      setToken(res.token);
      location.href = '/index.html';
    }catch(err){ alert(err.message); }
  };
}

async function loadProfile(){
  const box = qs('#profileInfo'); if (!box) return;
  try{
    const me = await fetchJSON(API.users.me, { headers: { ...authHeaders() } });
    box.innerHTML = `<div><strong>Username:</strong> ${me.username}</div>
                     <div><strong>Email:</strong> ${me.email}</div>
                     <div><strong>Role:</strong> ${me.role}</div>`;
  }catch(err){ location.href = '/login.html'; }
}

function parseQuery(){ const u = new URL(location.href); return Object.fromEntries(u.searchParams.entries()); }

async function loadMovieDetail(){
  const container = qs('#movieDetail'); if (!container) return;
  const { id } = parseQuery();
  const data = await fetchJSON(`${API.movies}/${id}`);
  container.innerHTML = `
    <div style="display:flex; gap:16px; flex-wrap:wrap;">
      <img style="width:240px; height:360px; object-fit:cover; border-radius:8px" src="${data.poster_url || 'https://picsum.photos/400/600?random='+data.movie_id}">
      <div>
        <h2>${data.title}</h2>
        <div class="movie-info">
          <div><strong>Genre:</strong> ${data.genre || 'N/A'}</div>
          <div><strong>Year:</strong> ${data.release_year || 'N/A'}</div>
          <div><strong>Director:</strong> ${data.director || 'N/A'}</div>
          <div><strong>Rating:</strong> ⭐ ${data.average_rating?.toFixed(1) ?? 'No ratings'}</div>
        </div>
        <div class="movie-description">
          <h3>Description</h3>
          <p>${data.description || 'No description available.'}</p>
        </div>
      </div>
    </div>`;
  const list = qs('#reviews');
  list.innerHTML = (data.reviews||[]).map(r => `
    <div class="card" style="margin:8px 0">
      <div><strong>${r.username}</strong> • ⭐ ${r.rating}</div>
      <div>${r.comment || ''}</div>
    </div>`).join('');

  const form = qs('#reviewForm');
  form.onsubmit = async (e)=>{
    e.preventDefault();
    const payload = { movie_id: Number(id), rating: Number(qs('#reviewRating').value), comment: qs('#reviewComment').value };
    try{
      await fetchJSON(API.reviews, { method:'POST', headers: { ...authHeaders() }, body: JSON.stringify(payload) });
      location.reload();
    }catch(err){ alert(err.message); if (err.message.includes('401')) location.href = '/login.html'; }
  };
}

window.addEventListener('DOMContentLoaded', ()=>{
  updateNav();
  const searchBtn = qs('#searchBtn'); if (searchBtn) searchBtn.onclick = loadMovies;
  loadMovies();
  handleLogin();
  handleRegister();
  loadProfile();
  loadMovieDetail();
});


