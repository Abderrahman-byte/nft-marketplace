import React from 'react'
import { Route, Routes } from 'react-router'

import AppFooter from './components/AppFooter'
import AppHeader from './components/AppHeader'
import RegisterPage from './pages/register.page'

import './styles/App.css'

function App() {
	return (
		<div className='App'>
			<AppHeader />
			<Routes>
				<Route path='/sign-up' element={<RegisterPage />} />
			</Routes>
			<AppFooter />
		</div>
	)
}

export default App
