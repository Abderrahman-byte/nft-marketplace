import React from 'react'
import { Route, Routes } from 'react-router'

import AppFooter from './components/AppFooter'
import AppHeader from './components/AppHeader'
import RegisterPage from './pages/register.page'
import LoginPage from './pages/login.page'


import './styles/App.css'

function App() {
	return (
		<div className='App'>
			<AppHeader />
			<Routes>
				<Route path='/sign-up' element={<RegisterPage />} />
			</Routes>
			<Routes>
				<Route path='/sign-in' element={<LoginPage />} />
			</Routes>
		
			<AppFooter />
		</div>
	)
}

export default App
