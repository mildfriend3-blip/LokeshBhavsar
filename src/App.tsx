import React, { useState } from 'react';
import { GrievanceTicket, CivicCategory } from './types';

const INITIAL_TICKETS: GrievanceTicket[] = [
  {
    id: "HYE-0052",
    category: "Sadak (Potholes)",
    categoryHi: "सड़क पर गड्ढा",
    desc: "Severe asphalt rupture with stagnant water pool near Canal Road bend causing two-wheeler hazard.",
    location: "Ward 12 · Trikuta Nagar Canal Rd, Sec 3",
    coords: "32.7042° N, 74.8710° E",
    status: "SEALED",
    timestamp: "14:12:08 IST",
    hash: "SHA-256: 8F2A-91C8",
    clarity: "93.8% PASS",
    sizeKb: 48
  },
  {
    id: "HYE-0051",
    category: "Kachra (Garbage)",
    categoryHi: "सफाई एवं कचरा",
    desc: "Corridor municipal bin overflow outside Sector 4 primary enclosure blocking carriageway.",
    location: "Ward 12 · Trikuta Nagar, Sector 4",
    coords: "32.7058° N, 74.8732° E",
    status: "QUEUED",
    timestamp: "13:38:40 IST",
    hash: "SHA-256: 4A7B-89E1",
    clarity: "95.2% PASS",
    sizeKb: 52
  },
  {
    id: "HYE-0050",
    category: "Nala (Drainage)",
    categoryHi: "नाली जाम एवं जलभराव",
    desc: "Stormwater conduit choked by silt and building debris. Blackwater backing up into lane 6.",
    location: "Ward 14 · Gandhi Nagar Block C",
    coords: "32.7115° N, 74.8690° E",
    status: "QUEUED",
    timestamp: "12:45:15 IST",
    hash: "SHA-256: 9F1E-23B4",
    clarity: "91.4% PASS",
    sizeKb: 44
  },
  {
    id: "HYE-0049",
    category: "Bijli (Streetlights)",
    categoryHi: "स्ट्रीट लाइट बंद",
    desc: "Three consecutive sodium vapor lamps dead along park perimeter causing blind zone.",
    location: "Ward 8 · Bakshi Nagar Enclave",
    coords: "32.7290° N, 74.8510° E",
    status: "SYNCED",
    timestamp: "10:15:30 IST",
    hash: "SHA-256: 3D4E-7B21",
    clarity: "92.0% PASS",
    sizeKb: 39
  },
  {
    id: "HYE-0048",
    category: "Paani (Water Leakage)",
    categoryHi: "पेयजल पाइपलाइन रिसाव",
    desc: "Underground main burst spraying continuous clean water over roadway macadam.",
    location: "Ward 17 · Channi Himmat Main Chowk",
    coords: "32.6934° N, 74.8912° E",
    status: "SYNCED",
    timestamp: "08:24:10 IST",
    hash: "SHA-256: 7A1C-99B2",
    clarity: "96.5% PASS",
    sizeKb: 51
  }
];

const CATEGORIES: CivicCategory[] = [
  { id: "sadak", en: "Sadak (Potholes)", hi: "सड़क पर गड्ढा", presetDesc: "Deep road pothole (approx 1.2m) with exposed base gravel near Canal Rd bend." },
  { id: "kachra", en: "Kachra (Garbage)", hi: "सफाई एवं कचरा", presetDesc: "Solid municipal waste accumulation spilling outside Sector 4 enclosure." },
  { id: "bijli", en: "Bijli (Streetlights)", hi: "स्ट्रीट लाइट बंद", presetDesc: "Overhead sodium fixture out, creating complete corridor blackout at dusk." },
  { id: "paani", en: "Paani (Water Leakage)", hi: "पेयजल पाइपलाइन रिसाव", presetDesc: "High pressure potable pipeline joint leak flooding macadam street." },
  { id: "nala", en: "Nala (Drainage)", hi: "नाली जाम एवं जलभराव", presetDesc: "Silt obstruction in stormwater culvert with backflow into residential lane." }
];

export const App: React.FC = () => {
  const [isOnline, setIsOnline] = useState<boolean>(false);
  const [activeTab, setActiveTab] = useState<'citizen' | 'vault' | 'command'>('citizen');

  // Citizen form state
  const [selectedCat, setSelectedCat] = useState<CivicCategory>(CATEGORIES[0]);
  const [desc, setDesc] = useState<string>(CATEGORIES[0].presetDesc);
  const [cameraSimulated, setCameraSimulated] = useState<boolean>(false);
  const [cameraLoading, setCameraLoading] = useState<boolean>(false);
  const [toast, setToast] = useState<string | null>(null);

  // Vault state
  const [tickets, setTickets] = useState<GrievanceTicket[]>(INITIAL_TICKETS);
  const [syncProgress, setSyncProgress] = useState<number>(0);
  const [isSyncing, setIsSyncing] = useState<boolean>(false);
  const [syncLogs, setSyncLogs] = useState<string[]>([
    "[14:30:10] Hardware Keyring initialized: ChaCha20-Poly1305 AEAD",
    "[14:30:12] On-device SQLite ledger active: /data/secure/hyperedge.db",
    "[14:30:15] Signal status: DEAD-ZONE ACTIVE (Buffer mode engaged)"
  ]);

  const triggerToast = (msg: string) => {
    setToast(msg);
    setTimeout(() => setToast(null), 3500);
  };

  const handleSimulateCamera = () => {
    setCameraLoading(true);
    setTimeout(() => {
      setCameraLoading(false);
      setCameraSimulated(true);
      triggerToast("✓ Edge AI Photometry & TFLite validation passed!");
    }, 800);
  };

  const handleSaveToVault = () => {
    const newTicket: GrievanceTicket = {
      id: `HYE-00${tickets.length + 48}`,
      category: selectedCat.en,
      categoryHi: selectedCat.hi,
      desc: desc || selectedCat.presetDesc,
      location: "Ward 12 · Trikuta Nagar, Sector 4",
      coords: "32.7058° N, 74.8732° E",
      status: "SEALED",
      timestamp: new Date().toLocaleTimeString('en-IN', { hour12: false }) + " IST",
      hash: `SHA-256: ${(Math.random() * 0xFFFFFF << 0).toString(16).toUpperCase()}`,
      clarity: "93.8% PASS",
      sizeKb: 47
    };

    setTickets([newTicket, ...tickets]);
    setCameraSimulated(false);
    triggerToast("🔒 Ticket Sealed & Committed to ChaCha20 Encrypted SQLite Ledger!");
    setActiveTab('vault');
  };

  const handleTriggerSync = () => {
    if (!isOnline) {
      triggerToast("⚠ Signal is in Dead-Zone. Toggle to SIGNAL RESTORED first!");
      return;
    }

    setIsSyncing(true);
    setSyncProgress(15);
    setSyncLogs(prev => ["[14:40:01] Initiating JMC Central Handshake...", ...prev]);

    setTimeout(() => {
      setSyncProgress(45);
      setSyncLogs(prev => ["[14:40:02] Assembling ChaCha20 batch payload (144 KB)...", ...prev]);
    }, 800);

    setTimeout(() => {
      setSyncProgress(80);
      setSyncLogs(prev => ["[14:40:03] Delta-sync packets dispatched over JMC 4G backbone...", ...prev]);
    }, 1600);

    setTimeout(() => {
      setSyncProgress(100);
      setIsSyncing(false);
      setTickets(tickets.map(t => ({ ...t, status: 'SYNCED' })));
      setSyncLogs(prev => [
        "[14:40:04] Central Municipal ACK_JMC_9981 confirmed. 52/52 Tickets Reconciled!",
        ...prev
      ]);
      triggerToast("✔ All local cached records synchronized with JMC Command Cloud!");
    }, 2400);
  };

  const cachedCount = tickets.filter(t => t.status !== 'SYNCED').length;

  return (
    <div className="min-h-screen max-w-4xl mx-auto flex flex-col border-x border-[#0F2B46] bg-[#FAF6EE] shadow-2xl relative font-sans text-[#0F2B46]">
      
      {/* Paper Grain Overlay */}
      <div className="absolute inset-0 bg-[radial-gradient(#0F2B46_0.75px,transparent_0.75px)] [background-size:12px_12px] opacity-[0.035] pointer-events-none z-0"></div>

      {/* 1. TOP STICKY STATUS BAR */}
      <header className="sticky top-0 z-30 bg-[#0F2B46] text-white border-b border-[#0F2B46] shadow-sm">
        <div className="p-3 px-4 flex flex-wrap items-center justify-between gap-2">
          
          {/* Brand */}
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-white border-2 border-[#E8760C] flex flex-col items-center justify-center font-mono font-black text-[#0F2B46] leading-none text-[9px]">
              <span>JMC</span>
              <span className="text-[6px] text-[#E8760C]">जम्मू</span>
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="font-black text-sm tracking-wide">HyperEdge</span>
                <span className="text-xs text-slate-300 font-medium">| Smart City Jammu</span>
              </div>
              <div className="text-[9px] font-mono text-slate-300">
                Team HyperEdge · Central University of Jammu
              </div>
            </div>
          </div>

          {/* Interactive Signal Toggle */}
          <div className="flex items-center gap-2">
            <button
              onClick={() => setIsOnline(!isOnline)}
              className={`px-3 py-1.5 rounded-[4px] font-mono text-xs font-black tracking-wider flex items-center gap-2 transition-all transform hover:scale-[1.02] border ${
                isOnline 
                  ? 'bg-[#1F9D55] text-white border-[#1F9D55]' 
                  : 'bg-[#BE3A2B] text-white border-[#BE3A2B] -rotate-1'
              }`}
              title="Click to toggle network conditions"
            >
              <span className="w-2 h-2 rounded-full bg-white animate-pulse"></span>
              {isOnline ? "SIGNAL RESTORED (4G JMC Link)" : "OFFLINE MODE: ACTIVE (Dead-Zone)"}
            </button>
          </div>
        </div>

        {/* Telemetry Sub-strip */}
        <div className="bg-[#091A2B] px-4 py-1 flex items-center justify-between text-[10px] font-mono border-t border-slate-700/50 text-slate-300">
          <div className="flex items-center gap-3">
            <span className="flex items-center gap-1">
              <span className={`w-1.5 h-1.5 rounded-full ${isOnline ? 'bg-[#1F9D55]' : 'bg-[#E8760C]'}`}></span>
              SQLite Encrypted
            </span>
            <span className="text-[#E8760C] font-bold">ChaCha20-Poly1305</span>
            <span className="hidden sm:inline text-slate-400">Hardware Keyring Sealed</span>
          </div>
          <div className="text-[#1F9D55] font-bold">
            {isOnline ? "CORRIDOR 4G UP" : "STANDALONE MESH READY"}
          </div>
        </div>
      </header>

      {/* 2. MAIN NAVIGATION TABS */}
      <nav className="bg-white border-b border-[#0F2B46] sticky top-[69px] z-20 flex text-xs font-bold font-mono">
        <button
          onClick={() => setActiveTab('citizen')}
          className={`flex-1 py-3 px-2 text-center border-r border-[#0F2B46] transition-colors flex flex-col sm:flex-row items-center justify-center gap-1 ${
            activeTab === 'citizen'
              ? 'bg-[#E8760C] text-white'
              : 'bg-white text-[#0F2B46] hover:bg-slate-100'
          }`}
        >
          <span>Citizen Portal</span>
          <span className="text-[10px] opacity-80">(शिकायत दर्ज करें)</span>
        </button>

        <button
          onClick={() => setActiveTab('vault')}
          className={`flex-1 py-3 px-2 text-center border-r border-[#0F2B46] transition-colors flex items-center justify-center gap-2 ${
            activeTab === 'vault'
              ? 'bg-[#E8760C] text-white'
              : 'bg-white text-[#0F2B46] hover:bg-slate-100'
          }`}
        >
          <span>Offline Vault & Sync</span>
          <span className={`px-1.5 py-0.2 rounded-[4px] text-[10px] font-mono ${
            activeTab === 'vault' ? 'bg-[#0F2B46] text-white' : 'bg-[#E8760C] text-white'
          }`}>
            {cachedCount}
          </span>
        </button>

        <button
          onClick={() => setActiveTab('command')}
          className={`flex-1 py-3 px-2 text-center transition-colors flex items-center justify-center gap-1 ${
            activeTab === 'command'
              ? 'bg-[#E8760C] text-white'
              : 'bg-white text-[#0F2B46] hover:bg-slate-100'
          }`}
        >
          <span>JMC Command Console</span>
          <span className="text-[10px] hidden sm:inline">(Ward 12)</span>
        </button>
      </nav>

      {/* Toast Alert */}
      {toast && (
        <div className="fixed top-20 right-4 z-50 bg-[#0F2B46] text-white text-xs font-mono px-4 py-2.5 rounded-[4px] border-2 border-[#E8760C] shadow-lg flex items-center gap-2 animate-bounce">
          <span>{toast}</span>
        </div>
      )}

      {/* Main Container */}
      <main className="p-4 sm:p-6 flex-1 z-10">

        {/* SCREEN 1: CITIZEN PORTAL */}
        {activeTab === 'citizen' && (
          <div className="space-y-5">
            
            {/* Screen 1 Title */}
            <div className="border-b border-[#0F2B46] pb-3 flex flex-wrap items-baseline justify-between gap-2">
              <div>
                <h2 className="text-xl sm:text-2xl font-black tracking-tight text-[#0F2B46]">
                  File Grievance <span className="text-base text-slate-500 font-normal">/ शिकायत दर्ज करें</span>
                </h2>
                <p className="text-xs text-slate-600 font-mono mt-0.5">
                  JMC Civic Form · Node JMC-CORRIDOR-042 · Ward 12 (Trikuta Nagar)
                </p>
              </div>
              <span className="px-2 py-1 rounded-[4px] bg-[#1F9D55] text-white text-[10px] font-mono font-black">
                STANDALONE MESH SECURE
              </span>
            </div>

            {/* Bilingual Category Buttons */}
            <div className="space-y-2">
              <div className="flex items-center justify-between text-xs font-mono font-bold">
                <span>1. SELECT GRIEVANCE DOMAIN (शिकायत श्रेणी चुनें)</span>
                <span className="text-[#E8760C] text-[10px]">REQUIRED</span>
              </div>

              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-2">
                {CATEGORIES.map(cat => {
                  const isSelected = selectedCat.id === cat.id;
                  return (
                    <button
                      key={cat.id}
                      onClick={() => {
                        setSelectedCat(cat);
                        setDesc(cat.presetDesc);
                        setCameraSimulated(false);
                      }}
                      className={`p-3 text-left rounded-[4px] transition-all border ${
                        isSelected
                          ? 'bg-[#0F2B46] text-white border-[#E8760C] ring-2 ring-[#E8760C]/40'
                          : 'bg-white text-[#0F2B46] border-[#0F2B46] hover:bg-slate-50'
                      }`}
                    >
                      <div className="font-bold text-sm">{cat.en}</div>
                      <div className={`text-xs ${isSelected ? 'text-slate-300' : 'text-slate-500'}`}>
                        {cat.hi}
                      </div>
                    </button>
                  );
                })}
              </div>
            </div>

            {/* AI Camera HUD Simulator */}
            <div className="space-y-2">
              <div className="flex items-center justify-between text-xs font-mono font-bold">
                <span>2. EDGE AI PHOTOMETRIC VALIDATION</span>
                <span className="text-[#1F9D55] text-[10px]">ON-DEVICE NPU</span>
              </div>

              <div className="border border-[#0F2B46] rounded-[4px] bg-[#091A2B] text-white p-4 relative overflow-hidden">
                <div className="relative min-h-[160px] flex flex-col justify-between">
                  
                  {/* Top HUD Info */}
                  <div className="flex justify-between items-start text-[10px] font-mono">
                    <div className="bg-[#0F2B46]/90 px-2 py-1 rounded-[4px] border border-slate-600">
                      {cameraSimulated ? "✓ CLEAR PHOTOMETRY · NO BLUR" : "AWAITING SENSOR CAPTURE"}
                    </div>
                    <div className="bg-[#E8760C] text-white font-black px-2 py-0.5 rounded-[4px] text-[9px]">
                      {cameraSimulated ? "TFLite Model v2.4 (Local)" : "READY"}
                    </div>
                  </div>

                  {/* AI Inspection Box */}
                  {cameraSimulated ? (
                    <div className="my-3 border-2 border-dashed border-[#1F9D55] bg-[#1F9D55]/10 p-3 rounded-[4px] flex flex-col sm:flex-row items-center justify-between gap-2">
                      <div>
                        <span className="bg-[#1F9D55] text-white text-[9px] font-mono font-bold px-1.5 py-0.5 rounded-[4px]">
                          DETECTED: {selectedCat.en.toUpperCase()}
                        </span>
                        <div className="text-xs font-mono text-slate-200 mt-1">
                          Clarity Score: <span className="text-[#1F9D55] font-bold">93.8% PASS</span> · Variance: 312.4
                        </div>
                      </div>
                      <div className="text-right text-[10px] font-mono text-slate-300">
                        <div>SHA-256: 8F2A-91C8-3D4E</div>
                        <div className="text-[#E8760C]">0 DUPLICATES IN WARD BUFFER</div>
                      </div>
                    </div>
                  ) : (
                    <div className="my-4 text-center text-slate-400 text-xs font-mono">
                      [Tap "Simulate Camera" below to execute on-device blur variance & TFLite detection]
                    </div>
                  )}

                  {/* Camera Bar */}
                  <div className="flex flex-wrap items-center justify-between gap-2 pt-2 border-t border-slate-700/60 text-[10px] font-mono">
                    <span>GPS LOCK: 32.7058° N, 74.8732° E</span>
                    <button
                      onClick={handleSimulateCamera}
                      disabled={cameraLoading}
                      className="px-3 py-1 bg-[#E8760C] hover:bg-[#d06909] text-white font-bold rounded-[4px] text-xs transition-colors"
                    >
                      {cameraLoading ? "Running Edge AI..." : cameraSimulated ? "Re-calibrate Photometry" : "📷 Simulate Camera"}
                    </button>
                  </div>
                </div>
              </div>
            </div>

            {/* GPS and Encryption particulars */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3 space-y-1">
                <div className="text-[11px] font-mono font-bold text-[#0F2B46] flex items-center justify-between">
                  <span>GEOSPATIAL EDGE LOCK (भू-स्थान)</span>
                  <span className="text-[#1F9D55] text-[9px]">LOCKED</span>
                </div>
                <div className="text-xs font-bold text-[#0F2B46]">
                  Ward 12 · Trikuta Nagar, Sector 4 / Zone 2
                </div>
                <div className="text-[10px] font-mono text-slate-500">
                  GPS: 32.7058° N, 74.8732° E · Accuracy ±2.4m · Jammu Municipality
                </div>
              </div>

              <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3 space-y-1">
                <div className="text-[11px] font-mono font-bold text-[#0F2B46] flex items-center justify-between">
                  <span>CRYPTOGRAPHIC ENVELOPE</span>
                  <span className="text-[#E8760C] text-[9px]">HARDWARE SEAL</span>
                </div>
                <div className="text-xs font-bold text-[#0F2B46]">
                  ChaCha20-Poly1305 AEAD + SHA-256
                </div>
                <div className="text-[10px] font-mono text-slate-500">
                  Hardware Keyring Sealed · On-device Flash Storage
                </div>
              </div>
            </div>

            {/* Description Textarea */}
            <div className="space-y-1">
              <label className="text-xs font-mono font-bold block text-[#0F2B46]">
                PARTICULARS (विवरण):
              </label>
              <textarea
                rows={3}
                value={desc}
                onChange={(e) => setDesc(e.target.value)}
                className="w-full p-2.5 text-xs text-[#0F2B46] bg-white border border-[#0F2B46] rounded-[4px] focus:outline-none focus:ring-1 focus:ring-[#0F2B46]"
              ></textarea>
            </div>

            {/* Big Submit Button */}
            <button
              onClick={handleSaveToVault}
              className="w-full py-3.5 bg-[#E8760C] hover:bg-[#cf6809] text-white font-mono font-black text-sm rounded-[4px] tracking-wider transition-transform active:scale-[0.99] border border-[#0F2B46] shadow-sm flex items-center justify-center gap-2"
            >
              <span>🔒 SAVE TO CHACHA20 ENCRYPTED VAULT</span>
            </button>
          </div>
        )}

        {/* SCREEN 2: OFFLINE VAULT & SYNC */}
        {activeTab === 'vault' && (
          <div className="space-y-5">
            
            {/* Header with Rubber Stamp */}
            <div className="border-b border-[#0F2B46] pb-3 flex flex-wrap items-center justify-between gap-3 relative">
              <div>
                <h2 className="text-xl sm:text-2xl font-black tracking-tight text-[#0F2B46]">
                  Offline Vault & Delta Sync <span className="text-base text-slate-500 font-normal">/ लोकल वॉल्ट</span>
                </h2>
                <p className="text-xs text-slate-600 font-mono mt-0.5">
                  On-Device SQLite Cryptographic Ledger · Zero Cloud Blocking
                </p>
              </div>

              <div className="border-2 border-[#BE3A2B] text-[#BE3A2B] rotate-3 bg-[#BE3A2B]/10 font-mono font-black px-3 py-1 text-xs tracking-wider">
                ★ SEALED · JMC CERTIFIED ★
              </div>
            </div>

            {/* Status Indicator */}
            <div className={`p-3 rounded-[4px] border ${
              isOnline 
                ? 'bg-[#1F9D55]/10 border-[#1F9D55] text-[#1F9D55]' 
                : 'bg-[#BE3A2B]/10 border-[#BE3A2B] text-[#BE3A2B]'
            }`}>
              <div className="flex items-center justify-between font-mono font-black text-xs">
                <span className="flex items-center gap-2">
                  <span className={`w-2 h-2 rounded-full ${isOnline ? 'bg-[#1F9D55]' : 'bg-[#BE3A2B]'}`}></span>
                  {isOnline ? "SIGNAL RESTORED: READY TO PUSH DELTA PACKETS" : "DEAD-ZONE ACTIVE: RECORDS BUFFERED SAFELY IN LOCAL FLASH"}
                </span>
                <span className="text-[10px] underline cursor-pointer" onClick={() => setIsOnline(!isOnline)}>
                  {isOnline ? "Simulate Dead-Zone" : "Simulate 4G Return"}
                </span>
              </div>
            </div>

            {/* Delta Sync Pipeline Action */}
            <div className="border border-[#0F2B46] rounded-[4px] bg-white p-4 space-y-3">
              <div className="flex flex-wrap items-center justify-between gap-2">
                <div>
                  <div className="text-xs font-mono font-bold text-[#0F2B46]">
                    DELTA SYNC PIPELINE (Handshake ➔ Batching ➔ Transmitting ➔ Synced)
                  </div>
                  <div className="text-[11px] text-slate-500 font-mono">
                    {cachedCount} items pending central dispatch · {tickets.length * 48} KB total payload
                  </div>
                </div>

                <button
                  onClick={handleTriggerSync}
                  disabled={isSyncing || cachedCount === 0}
                  className={`px-4 py-2 rounded-[4px] font-mono font-bold text-xs transition-colors border ${
                    cachedCount === 0
                      ? 'bg-[#1F9D55] text-white border-[#1F9D55] cursor-default'
                      : isOnline
                      ? 'bg-[#E8760C] hover:bg-[#cf6809] text-white border-[#0F2B46]'
                      : 'bg-slate-300 text-slate-600 border-slate-400 cursor-not-allowed'
                  }`}
                >
                  {cachedCount === 0 ? "✔ All Synced" : isSyncing ? "Syncing..." : "⚡ Trigger Delta Sync"}
                </button>
              </div>

              {/* Progress bar */}
              <div className="w-full bg-slate-200 h-2.5 rounded-[4px] overflow-hidden border border-slate-300">
                <div 
                  className="bg-[#1F9D55] h-full transition-all duration-500 font-mono text-[8px] text-white text-center"
                  style={{ width: `${cachedCount === 0 ? 100 : syncProgress}%` }}
                ></div>
              </div>
            </div>

            {/* Local Queued Tickets Table */}
            <div className="border border-[#0F2B46] rounded-[4px] bg-white overflow-hidden shadow-sm">
              <div className="bg-[#0F2B46] text-white px-3 py-2 font-mono font-bold text-xs flex justify-between items-center">
                <span>LOCAL ENCRYPTED TICKETS ({tickets.length})</span>
                <span className="text-[10px] text-slate-300">SQLite Table: `grievance_ledger`</span>
              </div>

              <div className="overflow-x-auto">
                <table className="w-full text-left text-xs font-mono">
                  <thead className="bg-[#FAF6EE] border-b border-[#0F2B46] text-[#0F2B46]">
                    <tr>
                      <th className="p-2.5">ID</th>
                      <th className="p-2.5">CATEGORY</th>
                      <th className="p-2.5">LOCATION</th>
                      <th className="p-2.5">TIMESTAMP</th>
                      <th className="p-2.5">STATUS</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-200">
                    {tickets.map((t, idx) => {
                      const isCritical = t.category.includes("Sadak") || t.category.includes("Paani");
                      return (
                        <tr key={t.id} className={idx % 2 === 0 ? "bg-white" : "bg-[#FAF6EE]/40"}>
                          <td className="p-2.5 font-bold text-[#0F2B46]">{t.id}</td>
                          <td className="p-2.5 font-medium">{t.category}</td>
                          <td className="p-2.5 text-slate-600 text-[11px]">{t.location}</td>
                          <td className="p-2.5 text-slate-500 text-[11px]">{t.timestamp}</td>
                          <td className="p-2.5">
                            <span className={`px-2 py-0.5 rounded-[4px] font-bold text-[10px] ${
                              t.status === 'SYNCED'
                                ? 'bg-[#1F9D55]/15 text-[#1F9D55] border border-[#1F9D55]'
                                : isCritical
                                ? 'bg-[#BE3A2B]/15 text-[#BE3A2B] border border-[#BE3A2B]'
                                : 'bg-[#E8760C]/15 text-[#E8760C] border border-[#E8760C]'
                            }`}>
                              {t.status}
                            </span>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Live Terminal Log */}
            <div className="space-y-1">
              <div className="text-xs font-mono font-bold text-[#0F2B46]">
                LIVE HARDWARE TELEMETRY LOGS:
              </div>
              <div className="bg-[#091A2B] text-slate-200 p-3 rounded-[4px] font-mono text-[11px] h-32 overflow-y-auto space-y-1 border border-[#0F2B46]">
                {syncLogs.map((log, i) => (
                  <div key={i} className={
                    log.includes("ACK") || log.includes("Reconciled") 
                      ? "text-[#1F9D55]" 
                      : log.includes("DEAD-ZONE") 
                      ? "text-[#BE3A2B]" 
                      : "text-slate-300"
                  }>
                    {log}
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* SCREEN 3: JMC COMMAND CONSOLE */}
        {activeTab === 'command' && (
          <div className="space-y-5">
            
            {/* Header with status badges */}
            <div className="border-b border-[#0F2B46] pb-3 flex flex-wrap items-center justify-between gap-2">
              <div>
                <h2 className="text-xl sm:text-2xl font-black tracking-tight text-[#0F2B46]">
                  JMC Command Console <span className="text-base text-slate-500 font-normal">/ नगर निगम कमांड</span>
                </h2>
                <p className="text-xs text-slate-600 font-mono mt-0.5">
                  Ward 12 (Trikuta Nagar) Incident Overview · Smart City Jammu
                </p>
              </div>

              <div className="flex items-center gap-2">
                <span className="px-2.5 py-1 rounded-[4px] text-[10px] font-mono font-black bg-[#1F9D55]/15 text-[#1F9D55] border border-[#1F9D55]">
                  City Centre (4G): ONLINE
                </span>
                <span className="px-2.5 py-1 rounded-[4px] text-[10px] font-mono font-black bg-[#BE3A2B]/15 text-[#BE3A2B] border border-[#BE3A2B]">
                  Hilly/Border Ward: DEAD-ZONE FALLBACK
                </span>
              </div>
            </div>

            {/* 4 Big Metrics Grid */}
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3 font-mono">
              <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3">
                <div className="text-[10px] font-bold text-slate-500">TOTAL REPORTS</div>
                <div className="text-4xl font-black text-[#0F2B46] mt-1">52</div>
                <div className="text-[9px] text-[#1F9D55] font-bold mt-1">+14 cached local</div>
              </div>

              <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3">
                <div className="text-[10px] font-bold text-slate-500">CACHED IN VAULT</div>
                <div className="text-4xl font-black text-[#E8760C] mt-1">{cachedCount}</div>
                <div className="text-[9px] text-[#BE3A2B] font-bold mt-1">4 critical potholes</div>
              </div>

              <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3">
                <div className="text-[10px] font-bold text-slate-500">RESOLVED FIELD</div>
                <div className="text-4xl font-black text-[#1F9D55] mt-1">38</div>
                <div className="text-[9px] text-slate-500 font-bold mt-1">73.1% resolution</div>
              </div>

              <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3">
                <div className="text-[10px] font-bold text-slate-500">AVG SLA DISPATCH</div>
                <div className="text-4xl font-black text-[#0F2B46] mt-1">18 <span className="text-sm font-normal text-slate-500">min</span></div>
                <div className="text-[9px] text-slate-500 font-bold mt-1">Auto NPU Routing</div>
              </div>
            </div>

            {/* Tawi Corridor GIS Map Schematic */}
            <div className="border border-[#0F2B46] rounded-[4px] bg-white p-4 space-y-2">
              <div className="flex justify-between items-center text-xs font-mono font-bold text-[#0F2B46]">
                <span>WARD 12 TAWI CORRIDOR MAP SCHEMATIC</span>
                <span className="text-[10px] text-slate-500">GPS Mesh Sync</span>
              </div>

              <div className="h-44 bg-[#EDE9DC] border border-[#0F2B46] rounded-[4px] relative overflow-hidden flex flex-col justify-between p-3 font-mono">
                {/* Tawi River */}
                <div className="absolute right-4 top-0 bottom-0 w-16 bg-[#98C5E2] border-x border-[#67A7CE] flex items-center justify-center transform rotate-6">
                  <span className="text-[10px] font-black text-[#1E5275] tracking-widest -rotate-90">
                    TAWI RIVER ──►
                  </span>
                </div>

                {/* Roads */}
                <div className="absolute left-6 top-8 right-24 h-2 bg-white border-y border-slate-500 transform -rotate-3"></div>
                <div className="absolute left-20 top-0 bottom-0 w-2.5 bg-white border-x border-slate-500"></div>

                {/* Street Labels */}
                <div className="z-10 text-[9px] bg-white/90 border border-slate-400 px-1.5 py-0.5 rounded-[4px] inline-block self-start">
                  Canal Rd
                </div>
                <div className="z-10 text-[9px] bg-white/90 border border-slate-400 px-1.5 py-0.5 rounded-[4px] inline-block self-center">
                  Trikuta Nagar Rd
                </div>

                {/* Pins */}
                <div className="z-10 flex gap-4">
                  <span className="bg-[#BE3A2B] text-white text-[9px] font-bold px-1.5 py-0.5 rounded-[4px] shadow">
                    ⚠ HYE-0052 (Pothole)
                  </span>
                  <span className="bg-[#E8760C] text-white text-[9px] font-bold px-1.5 py-0.5 rounded-[4px] shadow">
                    🗑 HYE-0051 (Garbage)
                  </span>
                </div>

                {/* YOU ARE HERE Marker */}
                <div className="z-10 self-end bg-[#0F2B46] text-white text-[9px] font-bold px-2 py-0.5 rounded-[4px] flex items-center gap-1 border border-white">
                  <span className="w-2 h-2 rounded-full bg-[#29B6F6] animate-ping"></span>
                  YOU ARE HERE (Sec 4)
                </div>
              </div>
            </div>

            {/* Municipal Field Dispatch Logs */}
            <div className="border border-[#0F2B46] rounded-[4px] bg-white p-3 space-y-2">
              <div className="text-xs font-mono font-bold text-[#0F2B46]">
                MUNICIPAL FIELD DISPATCH LOGS:
              </div>

              <div className="space-y-1.5 font-mono text-xs">
                <div className="p-2 border border-slate-200 rounded-[4px] flex justify-between items-center bg-[#FAF6EE]/50">
                  <div>
                    <span className="font-bold text-[#0F2B46]">HYE-0052: Sadak (Potholes)</span>
                    <div className="text-[10px] text-slate-500">Canal Rd bend · Assigned to Rapid Road Unit 4</div>
                  </div>
                  <span className="px-2 py-0.5 text-[9px] font-bold bg-[#BE3A2B]/15 text-[#BE3A2B] border border-[#BE3A2B]">
                    PRIORITY 1
                  </span>
                </div>

                <div className="p-2 border border-slate-200 rounded-[4px] flex justify-between items-center">
                  <div>
                    <span className="font-bold text-[#0F2B46]">HYE-0048: Paani (Water Leakage)</span>
                    <div className="text-[10px] text-slate-500">Channi Himmat Chowk · Repaired by Water Works Dept</div>
                  </div>
                  <span className="px-2 py-0.5 text-[9px] font-bold bg-[#1F9D55]/15 text-[#1F9D55] border border-[#1F9D55]">
                    RESOLVED
                  </span>
                </div>
              </div>
            </div>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="bg-[#0F2B46] text-white text-center py-2.5 px-4 text-[10px] font-mono border-t border-[#0F2B46] z-10">
        <div>HyperEdge · Team HyperEdge · Central University of Jammu · Smart City Jammu</div>
        <div className="text-slate-400 text-[8px] mt-0.5">v0.4.2 · build 128 · Offline-First Civic Framework</div>
      </footer>
    </div>
  );
};

export default App;
