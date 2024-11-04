import React from 'react';
import Sidebar from './Sidebar';
import CourseManagement from './CourseManagement';
import CourseCreate from '../course/CourseCreate';
import CourseUpdate from "../course/CourseUpdate";
import { Routes, Route } from 'react-router-dom';

function AdminDashboard() {
    return (
        <div style={adminDashboardStyle}>
            <div style={sidebarStyle}>
                <Sidebar />
            </div>
            <main style={mainContentStyle}>
                <Routes>
                    <Route path="courses-management" element={<CourseManagement />} />
                    <Route path="courses/create" element={<CourseCreate />} />
                    <Route path="courses/edit/:courseId" element={<CourseUpdate />} />
                </Routes>
            </main>
        </div>
    );
}

export default AdminDashboard;


const adminDashboardStyle = {
    display: 'flex',
    height: '100vh',
};

const sidebarStyle = {
    width: '250px',
    height: '100vh',
};

const mainContentStyle = {
    flex: 1,
    padding: '20px',
    marginRight: '200px',
    width: '100%',
    overflow: 'auto',
};
