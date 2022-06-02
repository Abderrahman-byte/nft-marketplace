import React, { useState, useEffect } from 'react'
import { Route, Routes, useParams } from 'react-router'
import { NavLink } from 'react-router-dom'

import SearchList from '@Components/SearchList'
import { searchCollections, searchItems, searchUsers } from '@Utils/api'
import TokensList from '@Components/TokensList'
import CollectionsList from '@Components/CollectionsList'

import '@Styles/SearchPages.css'
import UsersList from '@Components/UsersList'

const SearchPages = () => {
    const [query, setQuery] = useState()
    const params = useParams()

    useEffect(() => {
        setQuery(params?.query)
    }, [params])

    return (
        <div className='SearchPages page'>
            <h2>Search results for {query}</h2>

            <nav className='navbar'>
                <NavLink className='NavLink' to='./items'>Items</NavLink>
                <NavLink className='NavLink' to='./collections'>Collections</NavLink>
                <NavLink className='NavLink' to='./users'>Users</NavLink>
            </nav>

            <Routes>
                <Route path='items' element={<SearchList searchCallback={searchItems} container={TokensList} />} />
                <Route path='collections' element={<SearchList searchCallback={searchCollections} container={CollectionsList} />} />
                <Route path='users' element={<SearchList searchCallback={searchUsers} container={UsersList} />} />
            </Routes>
        </div>
    )
}

export default SearchPages