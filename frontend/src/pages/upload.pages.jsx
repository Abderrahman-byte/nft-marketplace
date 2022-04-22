import React from 'react'
import { Route, Routes } from 'react-router'

import UploadItemPage from './UploadItem.page'

const UploadPages = () => {
    return (
        <Routes>
            <Route index element={<UploadItemPage />} />
        </Routes>
    )
}

export default UploadPages