const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();
const db = admin.firestore();

exports.verifyTicket = functions.https.onRequest(async (req, res) => {
  const ticketId = req.query.ticket_id;
  if (!ticketId) {
    return res.status(400).send("No ticket ID provided.");
  }

  const ticketRef = db.collection("tickets").doc(ticketId);
  const ticketSnap = await ticketRef.get();

  if (!ticketSnap.exists) {
    return res.status(404).send("Ticket not found.");
  }

  const ticket = ticketSnap.data();

  if (ticket.status !== "valid") {
    return res.status(403).send("Ticket already used or invalid.");
  }

  // Update ticket as used
  await ticketRef.update({status: "used"});

  return res.status(200).send(`✅ Ticket valid for user: ${ticket.userId}`);
});

