import React from 'react'
import { Route, Routes } from 'react-router'

import UploadItemPage from '@Pages/UploadItem.page'
import UploadSinglePage from '@Pages/UploadSingle.page'

const UploadPages = () => {
    return (
        <Routes>
            <Route index element={<UploadItemPage />} />
            <Route path='single' element={<UploadSinglePage />} />
        </Routes>
    )
}

export default UploadPages