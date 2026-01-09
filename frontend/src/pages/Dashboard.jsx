import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Activity, Users, Skull, HeartPulse, AlertTriangle, Globe, LayoutDashboard, UserCircle, LogOut, FileText } from 'lucide-react';
import { XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from 'recharts';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [summary, setSummary] = useState([]);
  const [selectedCountry, setSelectedCountry] = useState(null);
  const [countryHistory, setCountryHistory] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [userProfile, setUserProfile] = useState(null);
  
  // Manage Data State
  const [manageType, setManageType] = useState('country-wise');
  const [manageData, setManageData] = useState([]);
  const [manageLoading, setManageLoading] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchData();
    fetchUserProfile();
  }, []);

  useEffect(() => {
    if (activeTab === 'manage') {
        fetchManageData();
    }
  }, [activeTab, manageType]);

  useEffect(() => {
    if (selectedCountry) {
        fetchHistory(selectedCountry.countryRegion);
    }
  }, [selectedCountry]);

  const fetchData = async () => {
    try {
      const [summaryRes, alertsRes] = await Promise.all([
        api.get('/api/dashboard/summary'),
        api.get('/api/dashboard/alerts')
      ]);
      setSummary(summaryRes.data);
      setAlerts(alertsRes.data);
      if (summaryRes.data.length > 0 && !selectedCountry) {
        setSelectedCountry(summaryRes.data[0]);
      }
    } catch (error) {
      console.error("Error fetching data", error);
    }
  };

  const fetchHistory = async (countryName) => {
      try {
          const res = await api.get(`/api/dashboard/history/${encodeURIComponent(countryName)}`);
          setCountryHistory(res.data);
      } catch (error) {
          console.error("Error fetching history", error);
      }
  };

  const fetchUserProfile = async () => {
    try {
        // Try to fetch current authenticated user first (works for OAuth2 and session)
        const res = await api.get('/api/user/me');
        setUserProfile(res.data);
        localStorage.setItem('userEmail', res.data.email);
    } catch(err) {
        console.log("Profile fetch from /me failed, trying localStorage", err);
        const email = localStorage.getItem('userEmail');
        if (email) {
            try {
                const res = await api.get(`/api/user/profile?email=${email}`);
                setUserProfile(res.data);
            } catch (e) {
                console.log("Profile fetch from email failed", e);
            }
        }
    }
  };

  const fetchManageData = async () => {
      setManageLoading(true);
      try {
          // Use ?page=0&size=100 generally to avoid crashing browser with huge datasets
          const res = await api.get(`/api/manage/${manageType}?page=0&size=100`);
          setManageData(res.data);
      } catch (error) {
          console.error("Error fetching manage data", error);
          alert("Failed to load data for " + manageType);
      } finally {
          setManageLoading(false);
      }
  };

  const handleLogout = () => {
    navigate('/login');
  };

  return (
    <div className="flex h-screen bg-slate-900 text-white overflow-hidden">
      {/* Sidebar */}
      <div className="w-64 bg-slate-950 border-r border-slate-800 p-4 flex flex-col">
        <div className="mb-8 px-2">
          <h1 className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-violet-400">
            Covid Analytics
          </h1>
        </div>
        
        <nav className="flex-1 space-y-2">
          <SidebarItem icon={LayoutDashboard} label="Dashboard" active={activeTab === 'dashboard'} onClick={() => setActiveTab('dashboard')} />
          <SidebarItem icon={FileText} label="Manage Data" active={activeTab === 'manage'} onClick={() => setActiveTab('manage')} />
          <SidebarItem icon={UserCircle} label="Profile" active={activeTab === 'profile'} onClick={() => setActiveTab('profile')} />
        </nav>

        <div className="mt-auto pt-4 border-t border-slate-800">
          <button onClick={handleLogout} className="flex items-center gap-3 w-full px-4 py-3 text-slate-400 hover:text-red-400 hover:bg-slate-900 rounded-xl transition-all">
            <LogOut className="w-5 h-5" />
            <span className="font-medium">Logout</span>
          </button>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 overflow-hidden relative flex flex-col">
          
        {activeTab === 'dashboard' && (
            <div className="flex-1 flex overflow-hidden">
                {/* Country List Sidebar */}
                <div className="w-80 bg-slate-900 border-r border-slate-800 flex flex-col">
                    <div className="p-4 border-b border-slate-800">
                        <h2 className="text-lg font-bold text-slate-200">Countries</h2>
                        <div className="mt-2 relative">
                             <p className="text-xs text-slate-500">{summary.length} countries loaded</p>
                        </div>
                    </div>
                    <div className="flex-1 overflow-y-auto p-2 space-y-1">
                        {summary.map((country, idx) => (
                            <button
                                key={idx}
                                onClick={() => setSelectedCountry(country)}
                                className={`w-full text-left px-4 py-3 rounded-lg transition-all flex justify-between items-center ${
                                    selectedCountry?.countryRegion === country.countryRegion
                                    ? 'bg-blue-600/20 text-blue-400 border border-blue-500/30'
                                    : 'text-slate-400 hover:bg-slate-800'
                                }`}
                            >
                                <span className="font-medium truncate">{country.countryRegion}</span>
                                {selectedCountry?.countryRegion === country.countryRegion && (
                                    <div className="w-2 h-2 rounded-full bg-blue-400" />
                                )}
                            </button>
                        ))}
                    </div>
                </div>

                {/* Country Detail View */}
                <div className="flex-1 overflow-y-auto p-8 bg-slate-900/50">
                    {selectedCountry ? (
                        <div className="max-w-5xl mx-auto space-y-8 animate-fade-in">
                            <header className="flex justify-between items-end">
                                <div>
                                    <h2 className="text-4xl font-bold text-white mb-2">{selectedCountry.countryRegion}</h2>
                                    <p className="text-slate-400">Latest statistics and historical trends</p>
                                </div>
                                <div className="text-right">
                                    <p className="text-sm text-slate-500">WHO Region</p>
                                    <p className="text-slate-300 font-medium">{selectedCountry.whoRegion}</p>
                                </div>
                            </header>

                            <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                                <StatCard title="Confirmed" value={selectedCountry.confirmed} icon={Users} color="text-blue-400" />
                                <StatCard title="Deaths" value={selectedCountry.deaths} icon={Skull} color="text-red-400" />
                                <StatCard title="Recovered" value={selectedCountry.recovered} icon={HeartPulse} color="text-green-400" />
                                <StatCard title="Active" value={selectedCountry.active} icon={Activity} color="text-yellow-400" />
                            </div>

                            {/* Charts Section */}
                            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                                <div className="lg:col-span-2 space-y-8">
                                    <div className="glass-panel p-6">
                                        <h3 className="text-xl font-bold mb-6 flex items-center gap-2">
                                            <Activity className="text-blue-400" />
                                            Case Progression
                                        </h3>
                                        <div className="h-[400px] w-full">
                                            {countryHistory.length > 0 ? (
                                                <ResponsiveContainer width="100%" height="100%">
                                                    <AreaChart data={countryHistory}>
                                                        <defs>
                                                            <linearGradient id="colorConfirmed" x1="0" y1="0" x2="0" y2="1">
                                                                <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.8}/>
                                                                <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
                                                            </linearGradient>
                                                            <linearGradient id="colorDeaths" x1="0" y1="0" x2="0" y2="1">
                                                                <stop offset="5%" stopColor="#ef4444" stopOpacity={0.8}/>
                                                                <stop offset="95%" stopColor="#ef4444" stopOpacity={0}/>
                                                            </linearGradient>
                                                        </defs>
                                                        <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                                                        <XAxis dataKey="date" stroke="#94a3b8" />
                                                        <YAxis stroke="#94a3b8" />
                                                        <Tooltip 
                                                            contentStyle={{ backgroundColor: '#1e293b', border: 'none', borderRadius: '8px', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)' }} 
                                                            itemStyle={{ color: '#e2e8f0' }} 
                                                        />
                                                        <Area type="monotone" dataKey="confirmed" stroke="#3b82f6" fillOpacity={1} fill="url(#colorConfirmed)" name="Confirmed" />
                                                        <Area type="monotone" dataKey="deaths" stroke="#ef4444" fillOpacity={1} fill="url(#colorDeaths)" name="Deaths" />
                                                    </AreaChart>
                                                </ResponsiveContainer>
                                            ) : (
                                                <div className="flex items-center justify-center h-full text-slate-500">
                                                    Loading history data...
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                {/* Alerts Section */}
                                <div className="space-y-6">
                                    <div className="glass-panel p-6 h-full overflow-y-auto max-h-[500px]">
                                        <h3 className="text-xl font-bold mb-6 flex items-center gap-2 text-red-400">
                                            <AlertTriangle />
                                            System Alerts
                                        </h3>
                                        <div className="space-y-4">
                                            {alerts.filter(a => a.country === selectedCountry.countryRegion).length > 0 ? (
                                                alerts.filter(a => a.country === selectedCountry.countryRegion).map((alert, idx) => (
                                                    <motion.div 
                                                        key={idx}
                                                        initial={{ opacity: 0, x: 20 }}
                                                        animate={{ opacity: 1, x: 0 }}
                                                        className={`p-4 rounded-lg border-l-4 ${alert.level === 'HIGH' ? 'bg-red-900/20 border-red-500' : 'bg-yellow-900/20 border-yellow-500'}`}
                                                    >
                                                        <div className="flex justify-between items-start">
                                                            <h4 className="font-bold text-white">{alert.country}</h4>
                                                            <span className={`text-xs px-2 py-1 rounded-full ${alert.level === 'HIGH' ? 'bg-red-500/20 text-red-300' : 'bg-yellow-500/20 text-yellow-300'}`}>
                                                                {alert.level}
                                                            </span>
                                                        </div>
                                                        <p className="text-sm text-slate-300 mt-1">{alert.message}</p>
                                                    </motion.div>
                                                ))
                                            ) : (
                                                <div className="p-4 rounded-lg bg-slate-800/50 border border-slate-700 text-slate-400 text-center text-sm">
                                                    No specific alerts for {selectedCountry.countryRegion}
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className="flex flex-col items-center justify-center h-full text-slate-500">
                            <Globe className="w-16 h-16 mb-4 opacity-20" />
                            <p className="text-xl">Select a country to view details</p>
                        </div>
                    )}
                </div>
            </div>
        )}

        {activeTab === 'manage' && (
            <div className="flex-1 flex flex-col p-8 overflow-hidden">
                <div className="max-w-7xl w-full mx-auto flex-1 flex flex-col animate-fade-in space-y-6 overflow-hidden">
                    <div className="flex justify-between items-center">
                        <h2 className="text-3xl font-bold">Manage Data</h2>
                        <select 
                            value={manageType}
                            onChange={(e) => setManageType(e.target.value)}
                            className="bg-slate-800 border border-slate-700 text-white rounded-lg px-4 py-2 hover:bg-slate-700 transition-colors cursor-pointer outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="country-wise">Country Wise Latest</option>
                            <option value="worldometer">Worldometer Data</option>
                            <option value="day-wise">Day Wise Trends</option>
                            <option value="clean-complete">Clean Complete</option>
                            <option value="full-grouped">Full Grouped History</option>
                        </select>
                    </div>

                    <div className="glass-panel p-0 flex-1 overflow-hidden relative">
                        {manageLoading ? (
                            <div className="flex items-center justify-center h-full text-slate-400">Loading data...</div>
                        ) : (
                           <DynamicTable data={manageData} type={manageType} onUpdate={fetchManageData} />
                        )}
                    </div>
                </div>
            </div>
        )}

        {activeTab === 'profile' && (
             <div className="p-8 overflow-y-auto h-full">
                 <div className="max-w-2xl mx-auto animate-fade-in space-y-6">
                    <h2 className="text-3xl font-bold mb-8">User Profile</h2>
                    <div className="glass-panel p-8">
                        <div className="flex items-center gap-6 mb-8">
                            <div className="w-24 h-24 bg-gradient-to-br from-blue-500 to-violet-600 rounded-full flex items-center justify-center text-3xl font-bold shadow-lg shadow-blue-500/20">
                                {userProfile?.name ? userProfile.name[0].toUpperCase() : (userProfile?.email ? userProfile.email[0].toUpperCase() : 'U')}
                            </div>
                            <div>
                                <h3 className="text-2xl font-bold text-white">{userProfile?.name || 'User Name'}</h3>
                                <p className="text-slate-400">{userProfile?.email || 'user@example.com'}</p>
                                <span className="inline-block mt-2 px-3 py-1 bg-green-500/20 text-green-400 text-xs rounded-full border border-green-500/20">
                                    Verified Administrator
                                </span>
                            </div>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div className="p-4 rounded-xl bg-slate-800/50 border border-slate-700/50">
                                <label className="text-xs font-bold text-slate-500 uppercase tracking-wider">Full Name</label>
                                <p className="text-lg text-slate-200 mt-1">{userProfile?.name || 'Not set'}</p>
                            </div>
                            <div className="p-4 rounded-xl bg-slate-800/50 border border-slate-700/50">
                                <label className="text-xs font-bold text-slate-500 uppercase tracking-wider">Email Address</label>
                                <p className="text-lg text-slate-200 mt-1">{userProfile?.email || 'user@example.com'}</p>
                            </div>
                            <div className="p-4 rounded-xl bg-slate-800/50 border border-slate-700/50">
                                <label className="text-xs font-bold text-slate-500 uppercase tracking-wider">Role</label>
                                <p className="text-lg text-slate-200 mt-1">Admin Access</p>
                            </div>
                            <div className="p-4 rounded-xl bg-slate-800/50 border border-slate-700/50">
                                <label className="text-xs font-bold text-slate-500 uppercase tracking-wider">Authentication</label>
                                <p className="text-lg text-green-400 mt-1 flex items-center gap-2">
                                    2FA Enabled <ShieldCheckIcon className="w-4 h-4" />
                                </p>
                            </div>
                        </div>

                        <div className="mt-12 pt-8 border-t border-slate-800">
                             <div className="bg-red-500/5 border border-red-500/20 rounded-2xl p-6">
                                <h4 className="text-xl font-bold text-red-400 mb-2">Danger Zone</h4>
                                <p className="text-slate-400 mb-6 text-sm">
                                    Once you delete your account, there is no going back. All your data and access will be permanently removed from our systems.
                                </p>
                                <button 
                                    onClick={async () => {
                                        if (window.confirm("ARE YOU SURE? This will permanently delete your account and all associated data.")) {
                                            try {
                                                await api.delete('/api/user/delete');
                                                localStorage.clear();
                                                alert("Account deleted successfully.");
                                                navigate('/login');
                                            } catch (err) {
                                                console.error("Deletion err", err);
                                                alert("Failed to delete account. Please try again.");
                                            }
                                        }
                                    }}
                                    className="px-6 py-3 bg-red-600/10 text-red-500 border border-red-600/20 rounded-xl font-bold hover:bg-red-600 hover:text-white transition-all duration-300"
                                >
                                    Delete My Account
                                </button>
                             </div>
                        </div>
                    </div>
                 </div>
             </div>
        )}

      </div>
    </div>
  );
};

// Sub-components
const SidebarItem = ({ icon: Icon, label, active, onClick }) => (
  <button 
    onClick={onClick}
    className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all ${
      active 
        ? 'bg-blue-500/10 text-blue-400 border border-blue-500/20' 
        : 'text-slate-400 hover:bg-slate-900 hover:text-slate-200'
    }`}
  >
    <Icon className="w-5 h-5" />
    <span className="font-medium">{label}</span>
  </button>
);

const ShieldCheckIcon = (props) => (
    <svg {...props} xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><path d="m9 12 2 2 4-4"/></svg>
);

const StatCard = ({ title, value, icon: Icon, color }) => (
  <motion.div 
    whileHover={{ y: -5 }}
    className="glass-panel p-6"
  >
    <div className="flex justify-between items-start">
      <div>
        <p className="text-slate-400 text-sm font-medium">{title}</p>
        <h3 className="text-3xl font-bold mt-2 text-white">{value?.toLocaleString() || 0}</h3>
      </div>
      <div className={`p-3 rounded-xl bg-slate-800/50 ${color}`}>
        <Icon className="w-6 h-6" />
      </div>
    </div>
  </motion.div>
);

const DynamicTable = ({ data, type, onUpdate }) => {
    if (!data || data.length === 0) return <div className="p-8 text-center text-slate-500">No data found in this dataset.</div>;

    // Determine ID field
    const getId = (row) => {
        if (type === 'country-wise' || type === 'worldometer') return row.countryRegion;
        if (type === 'day-wise') return row.date;
        return row.id;
    };

    // Columns to show (limit to first 5 keys + actions)
    const columns = Object.keys(data[0]).filter(k => k !== 'id' && !k.includes('id')).slice(0, 6);

    return (
        <div className="h-full overflow-auto">
            <table className="w-full text-left border-collapse">
                <thead className="sticky top-0 bg-slate-900 z-10">
                    <tr className="text-slate-400 border-b border-slate-700">
                        {columns.map(col => (
                             <th key={col} className="p-3 capitalize text-sm font-semibold">{col.replace(/([A-Z])/g, ' $1').trim()}</th>
                        ))}
                        <th className="p-3 text-right text-sm font-semibold">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {data.map((row, idx) => (
                        <DynamicRow key={idx} row={row} columns={columns} type={type} id={getId(row)} onUpdate={onUpdate} />
                    ))}
                </tbody>
            </table>
        </div>
    );
};

const DynamicRow = ({ row, columns, type, id, onUpdate }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editData, setEditData] = useState({ ...row });

    // Danger Detection Logic
    // Supports different field names across datasets (confirmed vs totalCases, etc)
    const getVal = (keyOptions) => {
        for (const key of keyOptions) {
            if (editData[key] !== undefined) return Number(editData[key]);
        }
        return 0;
    };

    const cases = getVal(['confirmed', 'totalCases', 'Confirmed']);
    const deaths = getVal(['deaths', 'totalDeaths', 'Deaths']);
    
    // Rule: If 100 cases found and 1 died -> Dangerous (User specified logic)
    const isDangerous = cases >= 100 && deaths >= 1;

    const handleSave = async () => {
        try {
            await api.put(`/api/manage/${type}/${encodeURIComponent(id)}`, editData);
            setIsEditing(false);
            onUpdate();
        } catch (error) {
            alert('Update failed');
            console.error(error);
        }
    };

    const handleDelete = async () => {
        if (confirm('Are you sure you want to delete this record?')) {
            try {
                await api.delete(`/api/manage/${type}/${encodeURIComponent(id)}`);
                onUpdate();
            } catch (error) {
                alert('Delete failed');
                console.error(error);
            }
        }
    };

    return (
        <tr className={`border-b border-slate-800/50 transition-colors group ${isDangerous ? 'bg-red-900/20 hover:bg-red-900/30 border-red-900/50' : 'hover:bg-slate-800/40'}`}>
            {columns.map(col => (
                <td key={col} className="p-3">
                    {isEditing ? (
                        <input 
                            className="bg-slate-900 border border-slate-600 rounded px-2 py-1 w-full text-sm text-white focus:border-blue-500 outline-none"
                            value={editData[col] || ''}
                            onChange={(e) => setEditData({...editData, [col]: e.target.value})}
                        />
                    ) : (
                        <div className="flex items-center gap-2">
                             <span className={`text-sm font-medium ${isDangerous ? 'text-red-200' : 'text-slate-300'}`}>
                                {typeof row[col] === 'number' ? row[col].toLocaleString() : (row[col]?.length > 30 ? row[col].substring(0,30)+'...' : row[col])}
                            </span>
                            {/* Show Warning Badge next to Country Name or ID if dangerous */}
                            {(col === 'countryRegion' || col === 'Country/Region' || col === 'provinceState' || col === 'date') && isDangerous && (
                                <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded text-[10px] font-bold bg-red-600 text-white animate-pulse">
                                    <AlertTriangle className="w-3 h-3" /> DON'T GO
                                </span>
                            )}
                        </div>
                    )}
                </td>
            ))}
            <td className="p-3 text-right space-x-2 min-w-[140px] opacity-0 group-hover:opacity-100 transition-opacity">
                {isEditing ? (
                    <>
                        <button onClick={handleSave} className="text-xs px-3 py-1 bg-green-500/20 text-green-400 rounded hover:bg-green-500/30 transition-colors cursor-pointer border border-green-500/20">Save</button>
                        <button onClick={() => setIsEditing(false)} className="text-xs px-3 py-1 bg-slate-700 text-slate-300 rounded hover:bg-slate-600 transition-colors cursor-pointer border border-slate-600">Cancel</button>
                    </>
                ) : (
                    <>
                        <button onClick={() => setIsEditing(true)} className="text-xs px-3 py-1 bg-blue-500/20 text-blue-400 rounded hover:bg-blue-500/30 transition-colors cursor-pointer border border-blue-500/20">Edit</button>
                        <button onClick={handleDelete} className="text-xs px-3 py-1 bg-red-500/20 text-red-400 rounded hover:bg-red-500/30 transition-colors cursor-pointer border border-red-500/20">Delete</button>
                    </>
                )}
            </td>
        </tr>
    );
};

export default Dashboard;
