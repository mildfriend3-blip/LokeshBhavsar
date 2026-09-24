import React, { useState, useEffect } from 'react';

export interface ReportItem {
  id: string;
  title: string;
  category: string;
  location: string;
  timeAgo: string;
  status: 'RESOLVED' | 'IN_PROGRESS' | 'QUEUED' | 'SYNCED';
  imageUrl: string;
  votes: number;
  aiConfidence: number;
  hash?: string;
}

const INITIAL_REPORTS: ReportItem[] = [
  {
    id: "HYE-204",
    title: "Deep Pothole with Rainwater Pool",
    category: "Road & Pothole",
    location: "Canal Rd, Sec 3, Trikuta Nagar",
    timeAgo: "12m ago",
    status: "QUEUED",
    imageUrl: "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=600&auto=format&fit=crop&q=80",
    votes: 18,
    aiConfidence: 94,
    hash: "8F2A-91C8"
  },
  {
    id: "HYE-203",
    title: "Overflowing Municipal Dumpster",
    category: "Sanitation",
    location: "Near Sector 4 Market, Trikuta Nagar",
    timeAgo: "45m ago",
    status: "IN_PROGRESS",
    imageUrl: "https://images.unsplash.com/photo-1532996122724-e3c354a0b15b?w=600&auto=format&fit=crop&q=80",
    votes: 34,
    aiConfidence: 96,
    hash: "4A7B-89E1"
  },
  {
    id: "HYE-198",
    title: "Underground Water Main Leak",
    category: "Water Supply",
    location: "Main Chowk, Channi Himmat",
    timeAgo: "2h ago",
    status: "RESOLVED",
    imageUrl: "https://images.unsplash.com/photo-1584467735815-f778f274e296?w=600&auto=format&fit=crop&q=80",
    votes: 52,
    aiConfidence: 91,
    hash: "7A1C-99B2"
  },
  {
    id: "HYE-195",
    title: "Unlit Streetlight Fixtures (x3)",
    category: "Streetlight",
    location: "Lane 4, Bakshi Nagar",
    timeAgo: "4h ago",
    status: "RESOLVED",
    imageUrl: "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
    votes: 9,
    aiConfidence: 98,
    hash: "3D4E-7B21"
  }
];

export const App: React.FC = () => {
  const [currentTab, setCurrentTab] = useState<'home' | 'camera' | 'vault'>('home');
  const [isOnline, setIsOnline] = useState(false);
  const [reports, setReports] = useState<ReportItem[]>(INITIAL_REPORTS);
  
  // Camera viewfinder state
  const [cameraScanning, setCameraScanning] = useState(true);
  const [cameraDetected, setCameraDetected] = useState(false);
  
  // Sync state
  const [isSyncing, setIsSyncing] = useState(false);
  const [syncProgress, setSyncProgress] = useState(0);
  const [toastMsg, setToastMsg] = useState<string | null>(null);

  const showToast = (msg: string) => {
    setToastMsg(msg);
    setTimeout(() => setToastMsg(null), 3000);
  };

  useEffect(() => {
    if (currentTab === 'camera') {
      setCameraScanning(true);
      setCameraDetected(false);
      const timer = setTimeout(() => {
        setCameraScanning(false);
        setCameraDetected(true);
      }, 900);
      return () => clearTimeout(timer);
    }
  }, [currentTab]);

  const handleSaveReportFromCamera = () => {
    const newReport: ReportItem = {
      id: `HYE-${Math.floor(205 + Math.random() * 50)}`,
      title: "Asphalt Fissure & Road Rut",
      category: "Road & Pothole",
      location: "Sector 4, Trikuta Nagar (Ward 12)",
      timeAgo: "Just now",
      status: isOnline ? "SYNCED" : "QUEUED",
      imageUrl: "https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=600&auto=format&fit=crop&q=80",
      votes: 1,
      aiConfidence: 94,
      hash: "9C4D-22E5"
    };
    setReports([newReport, ...reports]);
    showToast("✓ Encrypted & stored in ChaCha20 Offline Vault");
    setCurrentTab('vault');
  };

  const handleSyncVault = () => {
    if (!isOnline) {
      showToast("⚠ No Signal. Switch toggle to 4G Online first!");
      return;
    }
    setIsSyncing(true);
    setSyncProgress(25);
    setTimeout(() => setSyncProgress(60), 600);
    setTimeout(() => setSyncProgress(90), 1200);
    setTimeout(() => {
      setSyncProgress(100);
      setIsSyncing(false);
      setReports(reports.map(r => ({ ...r, status: r.status === 'QUEUED' ? 'SYNCED' : r.status })));
      showToast("✓ All queued reports uploaded to JMC Cloud!");
    }, 1800);
  };

  const queuedCount = reports.filter(r => r.status === 'QUEUED').length;

  return (
    <div className="w-full max-w-md h-screen bg-[#F9FAFB] shadow-2xl flex flex-col relative overflow-hidden text-slate-800 antialiased mx-auto border-x border-slate-200">
      
      {/* 1. TOP NATIVE APP BAR */}
      <header className="bg-[#0F2B46] text-white px-4 py-3 flex items-center justify-between shadow-md z-20">
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-full bg-white flex flex-col items-center justify-center border-2 border-[#E8760C] text-[#0F2B46] font-bold leading-none shadow-sm">
            <span className="text-[10px] tracking-tighter font-extrabold">JMC</span>
            <span className="text-[5.5px] text-[#E8760C] -mt-0.5">जम्मू</span>
          </div>
          <div>
            <h1 className="text-base font-bold tracking-tight text-white leading-tight">HyperEdge</h1>
            <p className="text-[10px] text-slate-300 font-medium leading-none">Jammu Smart City · Ward 12</p>
          </div>
        </div>

        <button
          onClick={() => setIsOnline(!isOnline)}
          className={`px-3 py-1 rounded-full text-xs font-semibold flex items-center gap-1.5 transition-all shadow-sm border ${
            isOnline 
              ? 'bg-[#1F9D55]/20 text-green-300 border-[#1F9D55]/40' 
              : 'bg-[#BE3A2B]/25 text-red-300 border-[#BE3A2B]/50'
          }`}
        >
          <span className={`w-2 h-2 rounded-full ${isOnline ? 'bg-[#1F9D55] animate-pulse' : 'bg-[#BE3A2B]'}`}></span>
          <span className="text-[11px]">{isOnline ? "Online 4G" : "Offline"}</span>
        </button>
      </header>

      {/* Dead-zone Banner */}
      {!isOnline && (
        <div className="bg-[#BE3A2B] text-white px-4 py-1.5 text-xs font-medium flex items-center justify-between shadow-inner">
          <span className="flex items-center gap-1.5">
            <span>Dead-Zone mode: SQLite ChaCha20 Active</span>
          </span>
          <span className="text-[10px] underline cursor-pointer" onClick={() => setIsOnline(true)}>Connect</span>
        </div>
      )}

      {/* Toast Alert */}
      {toastMsg && (
        <div className="absolute top-16 left-4 right-4 z-50 bg-slate-900 text-white px-4 py-3 rounded-xl shadow-xl flex items-center gap-2 text-xs font-medium border border-slate-700 animate-fade-in">
          <span>{toastMsg}</span>
        </div>
      )}

      {/* 2. SCROLLABLE BODY */}
      <main className="flex-1 overflow-y-auto p-4 space-y-4 pb-24">
        
        {/* TAB 1: CITIZEN FEED */}
        {currentTab === 'home' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-lg font-bold text-slate-900 tracking-tight">Ward 12 Feed</h2>
                <p className="text-xs text-slate-500">Trikuta Nagar & Canal Road</p>
              </div>
              <span className="bg-slate-200/80 text-slate-700 text-xs px-2.5 py-1 rounded-full font-semibold">
                {reports.length} Reports
              </span>
            </div>

            <div className="space-y-3">
              {reports.map(item => {
                const isQueued = item.status === 'QUEUED';
                const isResolved = item.status === 'RESOLVED';
                return (
                  <div key={item.id} className="bg-white rounded-xl border border-slate-200/80 shadow-sm overflow-hidden flex flex-col">
                    <div className="h-36 w-full relative bg-slate-100 overflow-hidden">
                      <img src={item.imageUrl} alt={item.title} className="w-full h-full object-cover" />
                      <div className="absolute top-2.5 left-2.5 bg-slate-900/80 text-white px-2 py-0.5 rounded-md text-[10px] font-semibold">
                        {item.category}
                      </div>
                      <div className="absolute top-2.5 right-2.5">
                        {isQueued && (
                          <span className="bg-[#E8760C] text-white px-2 py-0.5 rounded-md text-[10px] font-bold shadow-sm">
                            Queued in Vault
                          </span>
                        )}
                        {isResolved && (
                          <span className="bg-[#1F9D55] text-white px-2 py-0.5 rounded-md text-[10px] font-bold shadow-sm">
                            ✓ Resolved
                          </span>
                        )}
                        {item.status === 'IN_PROGRESS' && (
                          <span className="bg-blue-600 text-white px-2 py-0.5 rounded-md text-[10px] font-bold shadow-sm">
                            In Progress
                          </span>
                        )}
                        {item.status === 'SYNCED' && (
                          <span className="bg-emerald-600 text-white px-2 py-0.5 rounded-md text-[10px] font-bold shadow-sm">
                            Synced JMC
                          </span>
                        )}
                      </div>
                      <div className="absolute bottom-2 right-2.5 bg-black/65 text-white text-[9px] px-1.5 py-0.5 rounded font-mono">
                        AI: {item.aiConfidence}% Verified
                      </div>
                    </div>

                    <div className="p-3.5 space-y-2">
                      <div>
                        <div className="flex items-center justify-between">
                          <h3 className="text-sm font-bold text-slate-900 leading-snug">{item.title}</h3>
                          <span className="text-[10px] font-mono text-slate-400 font-semibold">{item.id}</span>
                        </div>
                        <div className="text-[11px] text-slate-500 mt-1">
                          {item.location} · {item.timeAgo}
                        </div>
                      </div>
                      <div className="pt-2 border-t border-slate-100 flex items-center justify-between text-xs text-slate-500">
                        <span>{item.votes} citizens impacted</span>
                        <span className="text-[10px] font-mono text-slate-400">JMC Ward 12</span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* TAB 2: CAMERA VIEWFINDER */}
        {currentTab === 'camera' && (
          <div className="space-y-4 flex flex-col h-full">
            <div>
              <h2 className="text-lg font-bold text-slate-900 tracking-tight">Camera Viewfinder</h2>
              <p className="text-xs text-slate-500">Edge AI Photometry & TFLite Pothole Detection</p>
            </div>

            <div className="relative rounded-2xl overflow-hidden bg-black aspect-[3/4] border-2 border-slate-800 shadow-lg flex flex-col justify-between p-4">
              <img 
                src="https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?w=800&auto=format&fit=crop&q=80" 
                alt="Road viewfinder"
                className="absolute inset-0 w-full h-full object-cover opacity-80"
              />

              <div className="relative z-10 flex items-center justify-between text-white text-[11px] font-mono">
                <div className="bg-black/60 px-2.5 py-1 rounded-md border border-white/10">
                  GPS: 32.7058°N, 74.8732°E
                </div>
                <div className="bg-black/60 px-2 py-1 rounded-md text-[10px] text-[#E8760C] font-bold border border-white/10">
                  TFLite NPU Active
                </div>
              </div>

              <div className="relative z-10 my-auto flex flex-col items-center justify-center">
                <div className="w-52 h-44 border-2 border-[#E8760C] rounded-lg relative flex flex-col justify-between p-2 shadow-[0_0_15px_rgba(232,118,12,0.4)]">
                  <div className="self-start bg-[#E8760C] text-white text-[9px] font-bold px-2 py-0.5 rounded shadow">
                    POTHOLE DETECTED · 94.2%
                  </div>
                  <div className="self-end bg-black/70 text-white text-[8px] font-mono px-1.5 py-0.5 rounded border border-white/20">
                    Clarity: 93.8% PASS
                  </div>
                </div>
                <span className="text-[10px] text-white/90 font-mono mt-2 bg-black/50 px-2 py-0.5 rounded">
                  Ward 12 · Trikuta Nagar Sector 4
                </span>
              </div>

              <div className="relative z-10 bg-black/75 p-2.5 rounded-xl border border-white/10 text-white text-[11px] font-mono flex items-center justify-between">
                <div>
                  <div className="text-[#1F9D55] font-bold">✓ Clarity: 93.8% PASS</div>
                  <div className="text-[9px] text-slate-300">SHA-256: 8F2A-91C8-3D4E</div>
                </div>
                <div className="text-right">
                  <div className="text-[9px] text-slate-400">Payload: 48 KB</div>
                  <div className="text-[9px] text-[#E8760C] font-semibold">ChaCha20 Envelope</div>
                </div>
              </div>
            </div>

            <button
              onClick={handleSaveReportFromCamera}
              className="w-full py-4 bg-[#E8760C] hover:bg-orange-600 active:scale-[0.98] text-white font-bold text-sm rounded-xl shadow-lg transition-all"
            >
              Secure in Offline Vault
            </button>
          </div>
        )}

        {/* TAB 3: OFFLINE VAULT */}
        {currentTab === 'vault' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-lg font-bold text-slate-900 tracking-tight">Offline Vault</h2>
                <p className="text-xs text-slate-500">ChaCha20 Encrypted Local Buffer</p>
              </div>
              <span className="bg-[#E8760C]/15 text-[#E8760C] font-bold text-xs px-2.5 py-1 rounded-full">
                {queuedCount} Pending
              </span>
            </div>

            <div className="bg-white rounded-xl border border-slate-200/80 p-4 shadow-sm space-y-3">
              <div className="flex items-center justify-between">
                <div>
                  <h3 className="text-sm font-bold text-slate-900">JMC Delta Sync Pipeline</h3>
                  <p className="text-[11px] text-slate-500">
                    {isOnline ? "4G link available to push buffered payloads" : "Waiting for network coverage in Ward 12"}
                  </p>
                </div>
              </div>

              {isSyncing && (
                <div className="space-y-1.5 pt-1">
                  <div className="w-full bg-slate-100 rounded-full h-2 overflow-hidden">
                    <div className="bg-[#1F9D55] h-full transition-all duration-300 rounded-full" style={{ width: `${syncProgress}%` }}></div>
                  </div>
                </div>
              )}

              <button
                onClick={handleSyncVault}
                disabled={isSyncing || queuedCount === 0}
                className={`w-full py-2.5 px-4 rounded-lg font-semibold text-xs transition-all ${
                  queuedCount === 0 
                    ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
                    : isOnline
                    ? 'bg-[#1F9D55] hover:bg-emerald-700 text-white shadow-md'
                    : 'bg-slate-200 text-slate-600 hover:bg-slate-300'
                }`}
              >
                {isSyncing ? "Syncing..." : queuedCount === 0 ? "✓ All Local Records Synced" : isOnline ? `Upload ${queuedCount} Buffered Reports Now` : "Simulate 4G & Upload"}
              </button>
            </div>

            <div className="space-y-2">
              <h3 className="text-xs font-bold text-slate-600 uppercase tracking-wider">Encrypted SQLite Records</h3>
              {reports.map(item => (
                <div key={item.id} className="bg-white rounded-xl border border-slate-200/80 p-3 shadow-sm flex items-center justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <img src={item.imageUrl} alt={item.title} className="w-12 h-12 rounded-lg object-cover bg-slate-100 shrink-0" />
                    <div>
                      <div className="text-xs font-bold text-slate-900 leading-tight">{item.title}</div>
                      <div className="text-[10px] text-slate-500 mt-0.5">{item.location}</div>
                      <div className="text-[9px] font-mono text-slate-400 mt-0.5">SHA: {item.hash || "8F2A-91C8"} · 48 KB</div>
                    </div>
                  </div>
                  <div className="text-right shrink-0">
                    <span className={`inline-block text-[10px] font-bold px-2 py-0.5 rounded-full ${
                      item.status === 'QUEUED' ? 'bg-orange-100 text-[#E8760C]' : 'bg-emerald-100 text-[#1F9D55]'
                    }`}>
                      {item.status === 'QUEUED' ? 'Waiting' : 'Synced'}
                    </span>
                    <div className="text-[9px] text-slate-400 font-mono mt-1">{item.timeAgo}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

      {/* 3. FIXED BOTTOM NAVIGATION BAR */}
      <nav className="absolute bottom-0 left-0 right-0 bg-white border-t border-slate-200/80 px-6 py-2 flex items-center justify-between z-30 shadow-md">
        <button
          onClick={() => setCurrentTab('home')}
          className={`flex flex-col items-center gap-1 ${
            currentTab === 'home' ? 'text-[#E8760C]' : 'text-slate-400 hover:text-slate-600'
          }`}
        >
          <span className="text-[10px] font-semibold">Home</span>
        </button>

        <button
          onClick={() => setCurrentTab('camera')}
          className="flex flex-col items-center -mt-5"
        >
          <div className="w-12 h-12 rounded-full bg-[#E8760C] text-white shadow-lg flex items-center justify-center border-4 border-[#F9FAFB]">
            <span className="text-lg">📷</span>
          </div>
          <span className="text-[10px] font-bold text-slate-700 mt-0.5">Report</span>
        </button>

        <button
          onClick={() => setCurrentTab('vault')}
          className={`flex flex-col items-center gap-1 ${
            currentTab === 'vault' ? 'text-[#E8760C]' : 'text-slate-400 hover:text-slate-600'
          }`}
        >
          <span className="text-[10px] font-semibold">My Vault ({queuedCount})</span>
        </button>
      </nav>
    </div>
  );
};

export default App;
