import React from 'react'
import { Route, Routes } from 'react-router'

import AppFooter from './components/AppFooter'
import AppHeader from './components/AppHeader'
import RegisterPage from './pages/Register.page'
import LoginPage from './pages/login.page'
import AuthenticationOnly from './components/AuthenticatedOnly'
import CreatePages from './pages/upload.pages'

import './styles/App.css'

function App() {
	return (
		<div className='App'>
			<AppHeader />
			<Routes>
				<Route path='/sign-up' element={<RegisterPage />} />
				<Route path='/sign-in' element={<LoginPage />} />
				<Route path='upload/*' element={<AuthenticationOnly><CreatePages /></AuthenticationOnly>} />
			</Routes>
		
			<AppFooter />
		</div>
	)
}

export default App
